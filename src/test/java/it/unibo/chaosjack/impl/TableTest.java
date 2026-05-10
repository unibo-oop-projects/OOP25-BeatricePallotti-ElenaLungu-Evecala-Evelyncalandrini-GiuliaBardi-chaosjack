package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.Deck;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.Hand;
import it.unibo.chaosjack.model.api.Partecipant;
import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.api.RoundResult.Outcome;
import it.unibo.chaosjack.model.api.SpecialRound;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.Table.State;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.api.Wallet;
import it.unibo.chaosjack.model.impl.HandImpl;
import it.unibo.chaosjack.model.impl.PlayerImpl;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.StandardCard;
import it.unibo.chaosjack.model.impl.Suit;
import it.unibo.chaosjack.model.impl.TableImpl;

/**
 * Test for method of TableImpl.
 */
class TableTest {
    private static final int INITIAL_BALANCE = 2000;
    private static final int INITIAL_POT = 0;
    private static final int STANDARD_BET = 100;
    private static final int HIGH_BET = 200;
    private static final int NEGATIVE_BET = -50;
    private static final int POSITIVE_BET = 50;
    private static final int IMPOSSIBLE_BET = 5000;

    private static final int BALANCE_AFTER_HIGH_BET = 1800;

    private static final int SCORE_ZERO = 0;
    private static final int SCORE_WINNING = 20;
    private static final int SCORE_LOSING = 17;
    private static final int SCORE_MID = 18;
    private static final int SCORE_HIGH = 19;
    private static final int SCORE_BLACKJACK = 21;
    private static final int SCORE_BUSTED = 25;
    private static final int SCORE_DEALER_BUSTED = 27;

    private static final int PAYOUT_WIN_SINGLE = 200;
    private static final int PAYOUT_WIN_DOUBLE = 400;
    private static final int PAYOUT_BONUS = 600;
    private static final int PAYOUT_BJ_BONUS = 1000;
    private static final int PAYOUT_LOSS = 0;

    private static final int ROUND_ONE = 1;
    private static final int ROUND_TWO = 2;
    private static final int DEFAULT_INCREMENT = 1;

    private static final boolean NOT_SAMECOLOR_CARD = false;
    private static final boolean SAMECOLOR_CARD = true;
    private static final String P1 = "Marameo";
    private static final String P2 = "Bob";

    private Table table;
    private Wallet wallet;
    private final List<String> players = List.of(P1, P2);

    @BeforeEach
    void setUp() {
        wallet = new Wallet() {
            private int balance = INITIAL_BALANCE;

            @Override
            public int getBalance() { 
                return balance; 
            }

            @Override
            public void addFunds(final int amount) { 
                balance += amount; 
            }

            @Override
            public boolean removeFunds(final int amount) {
                if (balance >= amount) { 
                    balance -= amount; 
                    return true; 
                }
                return false;
            }
        };
        final var score = Map.of(P1, SCORE_ZERO, P2, SCORE_ZERO);
        table = new TableImpl(wallet, players, createEngine(score, SCORE_ZERO, NOT_SAMECOLOR_CARD));
    }

    @Test
    void testInitialState() {
        assertEquals(State.FIRST_BET, table.getCurrentState());
        assertEquals(INITIAL_POT, table.getPot(), "the pot must be 0");
    }

    @Test
    void testBettingLogic() {
        table.placeBet(P1, HIGH_BET);
        assertEquals(HIGH_BET, table.getPot());
        assertEquals(BALANCE_AFTER_HIGH_BET, wallet.getBalance(), "the account holder balance must decrease");
        assertThrows(IllegalArgumentException.class, () -> table.placeBet(P1, NEGATIVE_BET));
    }

    @Test
    void testBettingLogicMultiplayer() {
        table.placeBet(P1, STANDARD_BET);
        assertEquals(STANDARD_BET, table.getPot());
        assertEquals(Table.State.FIRST_BET, table.getCurrentState());

        table.placeBet(P2, STANDARD_BET);
        assertEquals(STANDARD_BET * 2, table.getPot());
        assertEquals(Table.State.PLAYING, table.getCurrentState());
    }

    @Test
    void testStepPassageValidation() {
        assertThrows(IllegalStateException.class, table::stepPassage);

        table.placeBet(P2, STANDARD_BET);
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
        table.placeBet(P2, STANDARD_BET);
        table.stepPassage();
        assertThrows(IllegalStateException.class, () -> table.placeBet(P1, POSITIVE_BET));
    }

    @Test
    void testInsufficientFunds() {
        assertThrows(IllegalArgumentException.class, () -> table.placeBet(P2, IMPOSSIBLE_BET));
        assertEquals(INITIAL_POT, table.getPot(), "The plate must not increase if the funds are insufficient");
    }

    @Test
    void testGetWinnerPlayerWins() {
        final var score = Map.of(P1, SCORE_WINNING);
        final GameEngine winEngine = createEngine(score, SCORE_MID, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(winEngine, score);
        assertEquals(Outcome.PLAYER_WON, result.outcome());
        assertEquals(PAYOUT_WIN_SINGLE, result.getPayOut(), "the wins must be double value of the pot");
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getWinHistory().getOrDefault(P1, 0));
    }

    @Test
    void testWinnerMultiplePlayersTieAndWin() {
        final var score = Map.of(P1, SCORE_WINNING, P2, SCORE_WINNING);
        final GameEngine winEngine = createEngine(score, SCORE_MID, NOT_SAMECOLOR_CARD); 
        final RoundResult result = playRound(winEngine, score);
        assertEquals(Outcome.PLAYERS_PUSH, result.outcome());
        assertEquals(PAYOUT_WIN_DOUBLE, result.getPayOut(), "the wins must be double value of the pot");
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getPushHistory().getOrDefault(P1, 0));
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getPushHistory().getOrDefault(P2, 0));
    }

    @Test
    void testWinnerPushWithDealer() {
        final var score = Map.of(P1, SCORE_HIGH, P2, SCORE_HIGH);
        final GameEngine pushEngine = createEngine(score, SCORE_HIGH, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(pushEngine, score);
        assertEquals(Outcome.PUSH, result.outcome());
        assertEquals(PAYOUT_LOSS, result.getPayOut(), "Standard push with dealer");
        assertEquals(INITIAL_POT, table.geStatistics().getWinHistory().getOrDefault(P1, 0));
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getLossHistory().getOrDefault(P1, 0));
    }

    @Test
    void testWinnerDealerWins() {
        final var score = Map.of(P1, SCORE_MID, P2, SCORE_MID);
        final GameEngine lossEngine = createEngine(score, SCORE_WINNING, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(lossEngine, score);
        assertEquals(Outcome.DEALER_WON, result.outcome());
        assertEquals(PAYOUT_LOSS, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getLossHistory().getOrDefault(P1, 0));
    }

    @Test
    void testWinnerAllPlayerGoOut() {
        final var score = Map.of(P1, SCORE_BUSTED, P2, SCORE_BUSTED);
        final GameEngine outEngine = createEngine(score, SCORE_WINNING, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(outEngine, score);
        assertEquals(Outcome.DEALER_WON, result.outcome());
        assertEquals(PAYOUT_LOSS, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getLossHistory().getOrDefault(P1, 0));
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getLossHistory().getOrDefault(P2, 0));
    }

    @Test
    void testDealerGoOut() {
        final var score = Map.of(P1, SCORE_WINNING, P2, SCORE_LOSING);
        final GameEngine outDealerEngine = createEngine(score, SCORE_DEALER_BUSTED, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(outDealerEngine, score);
        assertEquals(Outcome.PLAYER_WON, result.outcome());
        assertEquals(PAYOUT_WIN_DOUBLE, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getWinHistory().getOrDefault(P1, 0));
        assertEquals(INITIAL_POT, table.geStatistics().getWinHistory().getOrDefault(P2, 0));
    }

    @Test
    void testOneWinsOneLoses() {
        final var score = Map.of(P1, SCORE_LOSING, P2, SCORE_WINNING);
        final GameEngine mixEngine = createEngine(score, SCORE_MID, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(mixEngine, score);
        assertEquals(Outcome.PLAYER_WON, result.outcome());
        assertEquals(PAYOUT_WIN_DOUBLE, result.getPayOut());
        assertEquals(INITIAL_POT, table.geStatistics().getWinHistory().getOrDefault(P1, 0));
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getWinHistory().getOrDefault(P2, 0));
    }

    @Test
    void testWinsPLayerWithBonus() {
        final var score = Map.of(P1, SCORE_WINNING, P2, SCORE_LOSING);
        final GameEngine bonusWinEngine = createEngine(score, SCORE_MID, SAMECOLOR_CARD);
        final RoundResult result = playRound(bonusWinEngine, score);
        assertEquals(Outcome.PLAYER_BONUS, result.outcome());
        assertEquals(PAYOUT_BONUS, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getBonusHistory().getOrDefault(P1, 0));
        assertEquals(INITIAL_POT, table.geStatistics().getBonusHistory().getOrDefault(P2, 0));
    }

    @Test
    void testWinnerPLayerBlackJack() {
        final var score = Map.of(P1, SCORE_BLACKJACK, P2, SCORE_WINNING);
        final GameEngine bjEngine = createEngine(score, SCORE_MID, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(bjEngine, score);
        assertEquals(Outcome.PLAYER_BLACKJACK, result.outcome());
        assertEquals(PAYOUT_BONUS, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getBlackHistory().getOrDefault(P1, 0));
        assertEquals(INITIAL_POT, table.geStatistics().getBlackHistory().getOrDefault(P2, 0));
    }

    @Test
    void testPLayerBlackJackBonus() {
        final var score = Map.of(P1, SCORE_BLACKJACK, P2, SCORE_WINNING);
        final GameEngine bjEngine = createEngine(score, SCORE_MID, SAMECOLOR_CARD);
        final RoundResult result = playRound(bjEngine, score);
        assertEquals(Outcome.BLACKJACK_BONUS, result.outcome());
        assertEquals(PAYOUT_BJ_BONUS, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getWinHistory().getOrDefault(P1, 0));
        assertEquals(DEFAULT_INCREMENT, table.geStatistics().getBlackBonusHistory().getOrDefault(P1, 0));
        assertEquals(INITIAL_POT, table.geStatistics().getBlackBonusHistory().getOrDefault(P2, 0));
    }

    @Test
    void testOtherGame() {
        table.placeBet(P1, STANDARD_BET);
        table.otherGame();

        assertEquals(INITIAL_POT, table.getPot(), "the pot must be empty");
        assertEquals(ROUND_TWO, table.geStatistics().getTotalRounds());
        assertEquals(State.FIRST_BET, table.getCurrentState());
    }

   @Test
   void testIncremetsRound() {
    assertEquals(ROUND_ONE, table.geStatistics().getTotalRounds());
    table.otherGame();
    assertEquals(ROUND_TWO, table.geStatistics().getTotalRounds());

   }

    @Test
    void testReset() {
        table.placeBet(P1, STANDARD_BET);
        table.reset();
        assertEquals(INITIAL_POT, table.getPot());
        assertEquals(ROUND_ONE, table.geStatistics().getTotalRounds());
    }

    private RoundResult playRound(final GameEngine engine, final Map<String, Integer> player) {
        table = new TableImpl(wallet, players, engine);
        for (final String playersName : player.keySet()) {
            table.placeBet(playersName, STANDARD_BET);
        }
        table.stepPassage();
        return table.getWinner();
    }

    private GameEngine createEngine(final Map<String, Integer> pScore, final int dScore, final boolean p1Card) {
        return new GameEngine() {

            @Override 
            public int getPlayerScore(final String name) { 
                return pScore.getOrDefault(name, 0); 
            }

            @Override
            public Hand getDealerHand() { 
                return new Hand() {
                    @Override
                    public int getScore() {
                        return dScore;
                    }

                    @Override
                    public void addCard(final Card card) {
                    }

                    @Override
                    public boolean sameColor(final List<Card> cards) {
                        return false;
                    }

                    @Override
                    public List<Card> getCards() {
                        return null;
                    }
                };
            }

            @Override
            public void nextTurn() {
            }

            @Override
            public Deck getDeck() { 
                return null; 
            }

            @Override
            public List<Partecipant> getPlayers() {
                final Player p1 = new PlayerImpl(P1, STANDARD_BET);
                final Player p2 = new PlayerImpl(P2, STANDARD_BET);

                final Suit p1Suit2 = p1Card ? Suit.CLUBS : Suit.HEARTS;
                p1.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p1.getHand().addCard(new StandardCard(Rank.TWO, p1Suit2));
                p2.getHand().addCard(new StandardCard(Rank.ACE, Suit.CLUBS));
                p2.getHand().addCard(new StandardCard(Rank.TWO, Suit.HEARTS));
                return List.of(p1, p2);
            }

            @Override
            public void stand() {
            }

            @Override
            public Partecipant getCurrentPlayer() {
                return null;
            }

            @Override
            public int currentScore(final HandImpl hand) {
                return 0;
            }

            @Override
            public void setSpecialRound(final SpecialRound specialRound) {
            }

            @Override
            public void dealerTurn() {
            }

            @Override
            public void setTable(final Table table) {
            }
        };
    }

}
