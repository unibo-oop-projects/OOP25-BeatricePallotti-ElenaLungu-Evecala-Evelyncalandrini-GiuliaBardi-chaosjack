package it.unibo.chaosjack.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import it.unibo.chaosjack.model.api.Deck;
import it.unibo.chaosjack.model.impl.StandardDeck;
import it.unibo.chaosjack.model.impl.StandardCard;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.RoyalFreezeTurn;
import it.unibo.chaosjack.model.impl.Suit;
import it.unibo.chaosjack.model.impl.YingYung;
import java.util.List;
import java.util.ArrayList;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.impl.GameEngineImpl;
import it.unibo.chaosjack.model.impl.NPCimpl;
import it.unibo.chaosjack.model.impl.PlayerImpl;
import it.unibo.chaosjack.model.api.CardModifier;
import it.unibo.chaosjack.model.api.Dealer;
import it.unibo.chaosjack.model.api.Partecipant;
import it.unibo.chaosjack.model.impl.DealerImpl;
import it.unibo.chaosjack.model.api.NPC;
import it.unibo.chaosjack.model.api.SpecialRound;
import it.unibo.chaosjack.model.api.Statistics;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.Table.State;
import it.unibo.chaosjack.model.impl.DoubleHeartsRule;

 /**
  * Test for GameEngineImpl class.
  */
 class GameEngineImplTest {
    private Dealer dealer;
    private GameEngine engine;
    private SpecialRound specialRound;
    private Table table;
    private String nameHuman;
    private String nameBot;

    @BeforeEach
    void setUp() {
       final Player human1;
       final NPC bot1;
       final Deck deck;
       nameHuman = "topolino";
       nameBot = "pippo";
       deck = new StandardDeck();
       dealer = new DealerImpl();
       human1 = new PlayerImpl(nameHuman, 1000);
       bot1 = new NPCimpl(nameBot, 1000);
       final List<Partecipant> players = new ArrayList<>();
       players.add(human1);
       players.add(bot1);
       table = createTable(Table.State.FIRST_BET); 
       engine = new GameEngineImpl(deck, players, dealer);
       specialRound = new DoubleHeartsRule();

    }

    @Test
    void testNextTurn() {
    engine.setTable(table);
    table.stepPassage();
    engine.nextTurn();
    assertEquals(nameHuman, engine.getCurrentPlayer().getName());

    engine.nextTurn();
    assertEquals(nameBot, engine.getCurrentPlayer().getName());

    final Table wrongTable = createTable(State.FIRST_BET);
    engine.setTable(wrongTable);
    assertThrows(IllegalStateException.class, () -> {
            engine.nextTurn();
        });
    }

    @Test
    void testDealerTurn() {

        final Table firstTable = createTable(Table.State.PLAYING);
        engine.setTable(firstTable);

        assertThrows(IllegalStateException.class, () -> {
            engine.dealerTurn();
        });

        final Table correctTable = createTable(State.DEALER_TURN);
        engine.setTable(correctTable);
        engine.dealerTurn();
        assertEquals(dealer, engine.getCurrentPlayer());
        assertFalse(dealer.getHand().getCards().isEmpty());

    }

    @Test
    void testGetDealerHand() {
        dealer.getHand().addCard(new StandardCard(Rank.FOUR, Suit.CLUBS, CardModifier.NONE));
        assertEquals(1, engine.getDealerHand().getCards().size());
    }

    @Test
     void testSetSpecialRoundAndCurrentScore() {
        engine.getPlayers().get(0).getHand().addCard(new StandardCard(Rank.TWO, Suit.HEARTS, CardModifier.NONE));

        engine.setSpecialRound(specialRound);
        int score = engine.currentScore(engine.getPlayers().get(0).getHand());
        assertEquals(4, score); 

        specialRound = null;
        engine.setSpecialRound(specialRound);
        score = engine.currentScore(engine.getPlayers().get(0).getHand());
        assertEquals(2, score);

        specialRound = new YingYung();
        engine.setSpecialRound(specialRound);
        score = engine.currentScore(engine.getPlayers().get(0).getHand());
        assertEquals(0, score);

        specialRound = new RoyalFreezeTurn();
        engine.getPlayers().get(0).getHand().addCard(new StandardCard(Rank.JACK, Suit.HEARTS, CardModifier.NONE));
        engine.setSpecialRound(specialRound);
        score = engine.currentScore(engine.getPlayers().get(0).getHand());
        assertEquals(2, score);
    }

    @Test
    void testGetPlayerScore() {
        engine.getPlayers().get(0).getHand().addCard(new StandardCard(Rank.THREE, Suit.SPADES, CardModifier.NONE));
        final int score = engine.getPlayerScore(engine.getPlayers().get(0).getName());
        assertEquals(3, score);
    }

    @Test
    void testTurnsBeyondLimits() {

        final Table secondTable = createTable(Table.State.DEALER_TURN);
        secondTable.stepPassage();
        assertEquals(Table.State.RESULTS, secondTable.getCurrentState());

    }

    @Test
    void testUnknownPlayer() {
        final int score = engine.getPlayerScore("paperino");
        assertEquals(0, score);
    }

    @Test
    void testStand() {
        engine.setTable(table);
        table.stepPassage();
        engine.nextTurn();
        assertEquals(nameHuman, engine.getCurrentPlayer().getName());

        engine.stand();
        assertEquals(nameBot, engine.getCurrentPlayer().getName());

        final Table thirdTable = createTable(Table.State.DEALER_TURN);
        engine.setTable(thirdTable);

        engine.stand();
        assertEquals(Table.State.RESULTS, thirdTable.getCurrentState());

    }

    private Table createTable(final Table.State initialState) {
     return new Table() {

        private State currentState = initialState;

        @Override
        public State getCurrentState() {
            return this.currentState;
        }

        @Override
        public void stepPassage() {
            if (this.currentState == State.FIRST_BET) {
                this.currentState = State.PLAYING;
            } else if (this.currentState == State.PLAYING) {
                this.currentState = State.FINAL_BET;
            } else if (this.currentState == State.FINAL_BET) {
                this.currentState = State.DEALER_TURN;
            } else if (this.currentState == State.DEALER_TURN) {
                this.currentState = State.RESULTS;
            } 
        }

        @Override
        public void reset() {

        }

        @Override
        public void otherGame() {

        }

        @Override
        public List<String> getPlayers() {
            return List.of();
        }

        @Override
        public void placeBet(final String playerName, final int amount) {

        }

        @Override
        public int getPot() {
            return 0;
        }

        @Override
        public RoundResult getWinner() {
            return null;
        }

        @Override
        public int getPlayerScore(final String playerName) {
            return 0;
        }

        @Override
        public int getDealerScore() {
            return 0;
        }

        @Override
        public int getWalletBalance(final String playerName) {
            return 0;
        }

        @Override
        public Statistics geStatistics() {
            return null;
        }

     };
    }

}
