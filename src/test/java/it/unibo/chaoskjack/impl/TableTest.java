package it.unibo.chaoskjack.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.chaosjack.model.api.Deck;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.api.RoundResult.Outcome;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.Table.State;
import it.unibo.chaosjack.model.api.TurnState;
import it.unibo.chaosjack.model.api.Wallet;
import it.unibo.chaosjack.model.impl.Hand;
import it.unibo.chaosjack.model.impl.Player;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.StandardCard;
import it.unibo.chaosjack.model.impl.Suit;
import it.unibo.chaosjack.model.impl.TableImpl;

public class TableTest {
    private Table table;
    private Wallet wallet;
    private final List<String> players = List.of("Marameo", "Bob");
    
    @BeforeEach
    void setUp(){
        wallet = new Wallet() {
            private int balance = 2000;
            @Override
            public int getBalance() {return balance;}
            @Override
            public void addFunds(int amount){balance += amount;}
            @Override
            public boolean removeFunds(int amount){
                if (balance >= amount) {balance -= amount; return true;}
                return false;
            }
        };

        table = new TableImpl(wallet, players, createEngine(0,0));
    }

    @Test
    void testInitialState(){
        assertEquals(State.FIRST_BET, table.getCurrentState());
        assertEquals(0, table.getPot(), "the pot must be 0");
    }

    @Test
    void testBettingLogic() {
        table.placeBet("Marameo", 200);
        assertEquals(200, table.getPot());
        assertEquals(1800, wallet.getBalance(), "the account holder balance must decrease");
        assertThrows(IllegalArgumentException.class, () ->table.placeBet("Marameo", -50));
    }

    @Test
    void testStepPassageValidation() {
        assertThrows(IllegalStateException.class, () -> table.stepPassage());

        table.placeBet("Bob", 100);
        table.stepPassage();
        assertEquals(State.PLAYING, table.getCurrentState());

        table.stepPassage();
        assertEquals(State.FINAL_BET, table.getCurrentState());

        table.stepPassage();
        assertEquals(State.DEALER_TURN, table.getCurrentState());

        table.stepPassage();
        assertEquals(State.RESULTS, table.getCurrentState());
    }

    @Test
    void testBettingInWrongState() {
        table.placeBet("Bob", 100);
        table.stepPassage();
        assertThrows(IllegalStateException.class, () -> table.placeBet("Marameo", 50));
    }

    @Test
    void testInsufficientFunds() {
        assertThrows(IllegalArgumentException.class, () -> table.placeBet("Bob", 5000));
        assertEquals(0, table.getPot(), "The plate must not increase if the funds are insufficient");
    }
    
    @Test
    void testGetWinnerPlayerWins() {
        GameEngine winEngine = createEngine(20,18);
        table = new TableImpl(wallet, List.of("Marameo"), winEngine);

        table.placeBet("Marameo", 100);
        table.stepPassage();

        RoundResult result = table.getWinner();
        assertEquals(Outcome.PLAYER_WON, result.outcome());
        assertEquals(200, result.getPayOut(), "the wins must be double value of the pot");
        assertEquals(1, table.geStatistics().getWinHistory().getOrDefault("Marameo", 0));
    }

    @Test
    void testWinnerMultiplePlayersTieAndWin() {
        GameEngine winEngine = createEngine(20,18);
        table = new TableImpl(wallet, List.of("Marameo", "Bob"), winEngine);

        table.placeBet("Marameo", 100);
        table.placeBet("Bob", 100);
        table.stepPassage();
        RoundResult result = table.getWinner();
        assertEquals(Outcome.PLAYERS_PUSH, result.outcome());
        assertEquals(400, result.getPayOut(), "the wins must be double value of the pot");
        assertEquals(1, table.geStatistics().getPushHistory().getOrDefault("Marameo", 0));
        assertEquals(1, table.geStatistics().getPushHistory().getOrDefault("Bob", 0));
    }

    @Test
    void testWinnerPushWithDealer() {
        GameEngine pushEngine = createEngine(19,19);
        table = new TableImpl(wallet, List.of("Marameo"), pushEngine);
        
        table.placeBet("Marameo", 100);
        table.stepPassage();

        RoundResult result = table.getWinner();
        assertEquals(Outcome.PUSH, result.outcome());
        assertEquals(0, result.getPayOut(), "Standard push with dealer");
        assertEquals(0, table.geStatistics().getWinHistory().getOrDefault("Marameo", 0));
        assertEquals(1, table.geStatistics().getLossHistory().getOrDefault("Marameo", 0));
    }

    @Test
    void testWinnerDealerWins() {
        GameEngine lossEngine = createEngine(18,20);
        table = new TableImpl(wallet, List.of("Marameo"), lossEngine);
        
        table.placeBet("Marameo", 100);
        table.stepPassage();

        RoundResult result = table.getWinner();
        assertEquals(Outcome.DEALER_WON, result.outcome());
        assertEquals(0, result.getPayOut());
        assertEquals(1, table.geStatistics().getLossHistory().getOrDefault("Marameo", 0));

    }

    @Test
    void testWinnerAllPlayerGoOut() {
        GameEngine outEngine = createEngine(25,20);
        table = new TableImpl(wallet, List.of("Marameo", "Bob"), outEngine);

        table.placeBet("Marameo", 100);
        table.placeBet("Bob", 100);
        table.stepPassage();

        RoundResult result = table.getWinner();
        assertEquals(Outcome.DEALER_WON, result.outcome());
        assertEquals(0, result.getPayOut());
        assertEquals(1, table.geStatistics().getLossHistory().getOrDefault("Marameo", 0));
        assertEquals(1, table.geStatistics().getLossHistory().getOrDefault("Bob", 0));
    }

    @Test
    void testDealerGoOut() {
        GameEngine outDealerEngine = new GameEngine() {
            @Override 
            public int getPlayerScore(String name) { 
                return name.equals("Marameo") ? 20 : 19; 
            }
            @Override
            public Hand getDealerHand() { 
                return new Hand() {
                    @Override
                    public int getScore() { return 27; }
                };
            }
            @Override
            public void changeState(TurnState newState) {}
            @Override
            public void nextTurn() {}
            @Override
            public Deck getDeck() { return null; }
            @Override
            public List<Player> getPlayers() { 
                Player p1 = new Player("Marameo", false, wallet, 100);
                Player p2 = new Player("Bob", false, wallet, 100);
                p1.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p1.getHand().addCard(new StandardCard(Rank.TWO, Suit.HEARTS));

                p2.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p2.getHand().addCard(new StandardCard(Rank.ACE, Suit.DIAMONDS));
                return List.of(p1,p2);
            }
            @Override
            public void hit() {}
            @Override
            public void stand() {}
            
        };
;
        table = new TableImpl(wallet, List.of("Marameo", "Bob"), outDealerEngine);

        table.placeBet("Marameo", 100);
        table.placeBet("Bob", 100);
        table.stepPassage();

        RoundResult result = table.getWinner();
        assertEquals(Outcome.PLAYER_WON, result.outcome());
        assertEquals(400, result.getPayOut());
        assertEquals(1, table.geStatistics().getWinHistory().getOrDefault("Marameo", 0));
        assertEquals(0, table.geStatistics().getWinHistory().getOrDefault("Bob", 0));
    }
    

    @Test
    void testOneWinsOneLoses() {
        GameEngine mixEngine = new GameEngine() {
            @Override 
            public int getPlayerScore(String name) { 
                return name.equals("Marameo") ? 17 : 20; 
            }
            @Override
            public Hand getDealerHand() { 
                return new Hand() {
                    @Override
                    public int getScore() { return 18; }
                };
            }
            @Override
            public void changeState(TurnState newState) {}
            @Override
            public void nextTurn() {}
            @Override
            public Deck getDeck() { return null; }
            @Override
            public List<Player> getPlayers() { 
                Player p1 = new Player("Marameo", false, wallet, 100);
                Player p2 = new Player("Bob", false, wallet, 100);
                p1.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p1.getHand().addCard(new StandardCard(Rank.TWO, Suit.HEARTS));
                p2.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                 p2.getHand().addCard(new StandardCard(Rank.ACE, Suit.DIAMONDS));
                return List.of(p1,p2);
            }
            @Override
            public void hit() {}
            @Override
            public void stand() {}
            
        };

        table = new TableImpl(wallet, List.of("Marameo", "Bob"), mixEngine);

        table.placeBet("Marameo", 100);
        table.placeBet("Bob", 100);
        table.stepPassage();

        RoundResult result = table.getWinner();

        assertEquals(Outcome.PLAYER_WON, result.outcome());
        assertEquals(400, result.getPayOut());
        assertEquals(0, table.geStatistics().getWinHistory().getOrDefault("Marameo", 0));
        assertEquals(1, table.geStatistics().getWinHistory().getOrDefault("Bob", 0));
    }

    @Test
    void testWinsPLayerWithBonus() {
        GameEngine bonusWinEngine = new GameEngine() {
            @Override 
            public int getPlayerScore(String name) { 
                return name.equals("Marameo") ? 19 : 18; 
            }
            @Override
            public Hand getDealerHand() { 
                return new Hand() {
                    @Override
                    public int getScore() { return 18; }
                };
            }
            @Override
            public void changeState(TurnState newState) {}
            @Override
            public void nextTurn() {}
            @Override
            public Deck getDeck() { return null; }
            @Override
            public List<Player> getPlayers() { 
                Player p1 = new Player("Marameo", false, wallet, 100);
                Player p2 = new Player("Bob", false, wallet, 100);
                p1.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p1.getHand().addCard(new StandardCard(Rank.TWO, Suit.CLUBS));
                p2.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p2.getHand().addCard(new StandardCard(Rank.TWO, Suit.HEARTS));
                return List.of(p1,p2);
            }
            @Override
            public void hit() {}
            @Override
            public void stand() {}
            
        };

        table = new TableImpl(wallet, List.of("Marameo", "Bob"), bonusWinEngine);

        table.placeBet("Marameo", 100);
        table.placeBet("Bob", 100);
        table.stepPassage();

        RoundResult result = table.getWinner();

        assertEquals(Outcome.PLAYER_BONUS, result.outcome());
        assertEquals(600, result.getPayOut());
        assertEquals(1, table.geStatistics().getBonusHistory().getOrDefault("Marameo", 0));
        assertEquals(0, table.geStatistics().getBonusHistory().getOrDefault("Bob", 0));
    }

    @Test
    void testWinnerPLayerBlackJack() {
        GameEngine bjEngine = new GameEngine() {
            @Override 
            public int getPlayerScore(String name) { 
                return name.equals("Marameo") ? 21 : 20; 
            }
            @Override
            public Hand getDealerHand() { 
                return new Hand() {
                    @Override
                    public int getScore() { return 18; }
                };
            }
            @Override
            public void changeState(TurnState newState) {}
            @Override
            public void nextTurn() {}
            @Override
            public Deck getDeck() { return null; }
            @Override
            public List<Player> getPlayers() { 
                Player p1 = new Player("Marameo", false, wallet, 100);
                Player p2 = new Player("Bob", false, wallet, 100);
                p1.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p1.getHand().addCard(new StandardCard(Rank.TWO, Suit.HEARTS));
                p2.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p2.getHand().addCard(new StandardCard(Rank.TWO, Suit.HEARTS));
                return List.of(p1,p2);
            }
            @Override
            public void hit() {}
            @Override
            public void stand() {}
            
        };

        table = new TableImpl(wallet, List.of("Marameo", "Bob"), bjEngine);

        table.placeBet("Marameo", 100);
        table.placeBet("Bob", 100);
        table.stepPassage();

        RoundResult result = table.getWinner();

        assertEquals(Outcome.PLAYER_BLACKJACK, result.outcome());
        assertEquals(600, result.getPayOut());
        assertEquals(1, table.geStatistics().getBlackHistory().getOrDefault("Marameo", 0));
        assertEquals(0, table.geStatistics().getBlackHistory().getOrDefault("Bob", 0));
    }

    @Test
    void testPLayerBlackJackBonus() {
        GameEngine bjEngine = new GameEngine() {
            @Override 
            public int getPlayerScore(String name) { 
                return name.equals("Marameo") ? 21 : 20; 
            }
            @Override
            public Hand getDealerHand() { 
                return new Hand() {
                    @Override
                    public int getScore() { return 18; }
                };
            }
            @Override
            public void changeState(TurnState newState) {}
            @Override
            public void nextTurn() {}
            @Override
            public Deck getDeck() { return null; }
            @Override
            public List<Player> getPlayers() { 
                Player p1 = new Player("Marameo", false, wallet, 100);
                Player p2 = new Player("Bob", false, wallet, 100);
                p1.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p1.getHand().addCard(new StandardCard(Rank.TWO, Suit.CLUBS));
                p2.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p2.getHand().addCard(new StandardCard(Rank.TWO, Suit.HEARTS));
                return List.of(p1,p2);
            }
            @Override
            public void hit() {}
            @Override
            public void stand() {}
            
        };

        table = new TableImpl(wallet, List.of("Marameo", "Bob"), bjEngine);

        table.placeBet("Marameo", 100);
        table.placeBet("Bob", 100);
        table.stepPassage();

        RoundResult result = table.getWinner();

        assertEquals(Outcome.BLACKJACK_BONUS, result.outcome());
        assertEquals(1000, result.getPayOut());
        assertEquals(1, table.geStatistics().getWinHistory().getOrDefault("Marameo", 0));
        assertEquals(1, table.geStatistics().getBlackBonusHistory().getOrDefault("Marameo", 0));
        assertEquals(0, table.geStatistics().getBlackBonusHistory().getOrDefault("Bob", 0));
    }

    @Test
    void testOtherGame() {
        table.placeBet("Marameo", 100);
        table.otherGame();

        assertEquals(0, table.getPot(), "the pot must be empty");
        assertEquals(2, table.geStatistics().getTotalRounds());
        assertEquals(State.FIRST_BET, table.getCurrentState());
    }

   @Test
   void testIncremetsRound() {
    assertEquals(1, table.geStatistics().getTotalRounds());
    table.otherGame();
    assertEquals(2, table.geStatistics().getTotalRounds());

   }

    @Test
    void testReset() {
        table.placeBet("Marameo", 100);
        table.reset();

        assertEquals(0, table.getPot());
        assertEquals(1, table.geStatistics().getTotalRounds());
    }

    
    private GameEngine createEngine(int pScore, int dScore) {
        return new GameEngine() {
            @Override 
            public int getPlayerScore(String name) { 
                return pScore; 
            }
            @Override
            public Hand getDealerHand() { 
                return new Hand() {
                    @Override
                    public int getScore() { return dScore; }
                };
            }
            @Override
            public void changeState(TurnState newState) {}
            @Override
            public void nextTurn() {}
            @Override
            public Deck getDeck() { return null; }
            @Override
            public List<Player> getPlayers() {
                Player p1 = new Player("Marameo", false, wallet, 100);
                Player p2 = new Player("Bob", false, wallet, 100);
                p1.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p1.getHand().addCard(new StandardCard(Rank.TWO, Suit.HEARTS));
                p2.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p2.getHand().addCard(new StandardCard(Rank.TWO, Suit.HEARTS));
                return List.of(p1,p2);
            }
            @Override
            public void hit() {}
            @Override
            public void stand() {}
            
        };
    }

}
