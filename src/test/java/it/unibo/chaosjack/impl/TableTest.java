package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
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
    private static final int ROUND_START = 0;
    private static final int DEFAULT_INCREMENT = 1;

    private static final boolean NOT_SAMECOLOR_CARD = false;
    private static final boolean SAMECOLOR_CARD = true;
    private static final String P1 = "Marameo";
    private static final String P2 = "Bob";

    private Table table;
    private final List<String> players = List.of(P1, P2);

    @BeforeEach
    void setUp() {
        final var score = Map.of(P1, SCORE_ZERO, P2, SCORE_ZERO);
        table = new TableImpl(players, createEngine(score, SCORE_ZERO, NOT_SAMECOLOR_CARD));
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
        //assertEquals(BALANCE_AFTER_HIGH_BET, table.getWalletBalance(P1), "the account holder balance must decrease");
        assertThrows(IllegalArgumentException.class, () -> table.placeBet(P1, NEGATIVE_BET));
    }

    @Test
    void testBettingLogicMultiplayer() {
        table.placeBet(P1, STANDARD_BET);
        assertEquals(STANDARD_BET, table.getPot());
        assertEquals(Table.State.FIRST_BET, table.getCurrentState());

        table.placeBet(P2, STANDARD_BET);
        assertEquals(STANDARD_BET * 2, table.getPot());
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
        table.stepPassage();
        table.stepPassage();
        assertThrows(IllegalStateException.class, () -> table.placeBet(P1, POSITIVE_BET));
    }

    @Test
    void testGetWinnerPlayerWins() {
        final var score = Map.of(P1, SCORE_WINNING);
        final GameEngine winEngine = createEngine(score, SCORE_MID, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(winEngine, score);

        assertEquals(Outcome.PLAYER_WON, result.outcome());
        assertEquals(PAYOUT_WIN_SINGLE, result.getPayOut(), "the wins must be double value of the pot");
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getWinHistory().getOrDefault(P1, 0));
    }

    @Test
    void testWinnerMultiplePlayersTieAndWin() {
        final var score = Map.of(P1, SCORE_WINNING, P2, SCORE_WINNING);
        final GameEngine winEngine = createEngine(score, SCORE_MID, NOT_SAMECOLOR_CARD); 
        final RoundResult result = playRound(winEngine, score);
        assertEquals(Outcome.PLAYERS_PUSH, result.outcome());
        assertEquals(PAYOUT_WIN_DOUBLE, result.getPayOut(), "the wins must be double value of the pot");
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getPushHistory().getOrDefault(P1, 0));
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getPushHistory().getOrDefault(P2, 0));
    }

    @Test
    void testWinnerPushWithDealer() {
        final var score = Map.of(P1, SCORE_HIGH, P2, SCORE_HIGH);
        final GameEngine pushEngine = createEngine(score, SCORE_HIGH, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(pushEngine, score);
        assertEquals(Outcome.PUSH, result.outcome());
        assertEquals(PAYOUT_LOSS, result.getPayOut(), "Standard push with dealer");
        assertEquals(INITIAL_POT, table.getStatistics().getWinHistory().getOrDefault(P1, 0));
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getLossHistory().getOrDefault(P1, 0));
    }

    @Test
    void testWinnerDealerWins() {
        final var score = Map.of(P1, SCORE_MID, P2, SCORE_MID);
        final GameEngine lossEngine = createEngine(score, SCORE_WINNING, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(lossEngine, score);
        assertEquals(Outcome.DEALER_WON, result.outcome());
        assertEquals(PAYOUT_LOSS, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getLossHistory().getOrDefault(P1, 0));
    }

    @Test
    void testWinnerAllPlayerGoOut() {
        final var score = Map.of(P1, SCORE_BUSTED, P2, SCORE_BUSTED);
        final GameEngine outEngine = createEngine(score, SCORE_WINNING, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(outEngine, score);
        assertEquals(Outcome.DEALER_WON, result.outcome());
        assertEquals(PAYOUT_LOSS, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getLossHistory().getOrDefault(P1, 0));
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getLossHistory().getOrDefault(P2, 0));
    }

    @Test
    void testDealerGoOut() {
        final var score = Map.of(P1, SCORE_WINNING, P2, SCORE_LOSING);
        final GameEngine outDealerEngine = createEngine(score, SCORE_DEALER_BUSTED, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(outDealerEngine, score);
        assertEquals(Outcome.PLAYER_WON, result.outcome());
        assertEquals(PAYOUT_WIN_DOUBLE, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getWinHistory().getOrDefault(P1, 0));
        assertEquals(INITIAL_POT, table.getStatistics().getWinHistory().getOrDefault(P2, 0));
    }

    @Test
    void testOneWinsOneLoses() {
        final var score = Map.of(P1, SCORE_LOSING, P2, SCORE_WINNING);
        final GameEngine mixEngine = createEngine(score, SCORE_MID, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(mixEngine, score);

        assertEquals(Outcome.PLAYER_WON, result.outcome());
        assertEquals(PAYOUT_WIN_DOUBLE, result.getPayOut());
        assertEquals(INITIAL_POT, table.getStatistics().getWinHistory().getOrDefault(P1, 0));
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getWinHistory().getOrDefault(P2, 0));
    }

    @Test
    void testWinsPLayerWithBonus() {
        final var score = Map.of(P1, SCORE_WINNING, P2, SCORE_LOSING);
        final GameEngine bonusWinEngine = createEngine(score, SCORE_MID, SAMECOLOR_CARD);
        final RoundResult result = playRound(bonusWinEngine, score);

        assertEquals(Outcome.PLAYER_BONUS, result.outcome());
        assertEquals(PAYOUT_BONUS, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getBonusHistory().getOrDefault(P1, 0));
        assertEquals(INITIAL_POT, table.getStatistics().getBonusHistory().getOrDefault(P2, 0));
    }

    @Test
    void testWinnerPLayerBlackJack() {
        final var score = Map.of(P1, SCORE_BLACKJACK, P2, SCORE_WINNING);
        final GameEngine bjEngine = createEngine(score, SCORE_MID, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(bjEngine, score);

        assertEquals(Outcome.PLAYER_BLACKJACK, result.outcome());
        assertEquals(PAYOUT_BONUS, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getBlackHistory().getOrDefault(P1, 0));
        assertEquals(INITIAL_POT, table.getStatistics().getBlackHistory().getOrDefault(P2, 0));
    }

    @Test
    void testPLayerBlackJackBonus() {
        final var score = Map.of(P1, SCORE_BLACKJACK, P2, SCORE_WINNING);
        final GameEngine bjEngine = createEngine(score, SCORE_MID, SAMECOLOR_CARD);
        final RoundResult result = playRound(bjEngine, score);
        assertEquals(Outcome.BLACKJACK_BONUS, result.outcome());
        assertEquals(PAYOUT_BJ_BONUS, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getWinHistory().getOrDefault(P1, 0));
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getBlackBonusHistory().getOrDefault(P1, 0));
        assertEquals(INITIAL_POT, table.getStatistics().getBlackBonusHistory().getOrDefault(P2, 0));
    }

    @Test
    void testTieResolvedByCardCountPlayerAgainstDealer() {
        final var score = Map.of(P1, 20, P2, 10);
        final var pCards = Map.of(P1, 2, P2, 2);

        final GameEngine engine = createEngineWithCards(score, pCards, 20, 3, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(engine, score);

        assertEquals(Outcome.PLAYER_WON, result.outcome());
        assertEquals(PAYOUT_WIN_DOUBLE, result.getPayOut());
    }

    @Test
    void testTieResolvedByCardCountDealerAgainstPlayer() {
        final var score = Map.of(P1, 20, P2, 10);
        final var pCards = Map.of(P1, 3, P2, 2);

        final GameEngine engine = createEngineWithCards(score, pCards, 20, 2, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(engine, score);

        assertEquals(Outcome.DEALER_WON, result.outcome());
    }

    @Test
    void testTieResolvedByCardCountPlayers() {
        final var score = Map.of(P1, 20, P2, 20);
        final var pCards = Map.of(P1, 2, P2, 3);

        final GameEngine engine = createEngineWithCards(score, pCards, 18, 2, NOT_SAMECOLOR_CARD);
        final RoundResult result = playRound(engine, score);

        assertEquals(Outcome.PLAYER_WON, result.outcome());
        assertEquals(PAYOUT_WIN_DOUBLE, result.getPayOut());
        assertEquals(DEFAULT_INCREMENT, table.getStatistics().getWinHistory().getOrDefault(P1, 0));
        assertEquals(INITIAL_POT, table.getStatistics().getWinHistory().getOrDefault(P2, 0));
    }

    @Test
    void testOtherGame() {
        table.placeBet(P1, STANDARD_BET);
        table.otherGame();

        assertEquals(INITIAL_POT, table.getPot(), "the pot must be empty");
        assertEquals(ROUND_ONE, table.getStatistics().getTotalRounds());
        assertEquals(State.FIRST_BET, table.getCurrentState());
    }

   @Test
   void testIncremetsRound() {
    assertEquals(ROUND_START, table.getStatistics().getTotalRounds());
    table.otherGame();
    assertEquals(ROUND_ONE, table.getStatistics().getTotalRounds());

   }

    @Test
    void testReset() {
        table.placeBet(P1, STANDARD_BET);
        table.reset();
        assertEquals(INITIAL_POT, table.getPot());
        assertEquals(ROUND_START, table.getStatistics().getTotalRounds());
    }

    private RoundResult playRound(final GameEngine engine, final Map<String, Integer> player) {
        table = new TableImpl(players, engine);
        for (final String playersName : player.keySet()) {
            table.placeBet(playersName, STANDARD_BET);
        }
        table.stepPassage();
        return table.getWinner().result();
    }

    private GameEngine createEngine(final Map<String, Integer> pScore, final int dScore, final boolean p1Card) {
        return createEngineWithCards(pScore, Map.of(P1, 2, P2, 2), dScore, 2, p1Card);
    }

    private GameEngine createEngineWithCards(
        final Map<String, Integer> pScore,
        final Map<String, Integer> pCardsCount,
        final int dScore, final int dCardsCount,
        final boolean p1Card
    ) {
        final Player p1 = new PlayerImpl(P1, INITIAL_BALANCE);
        final Player p2 = new PlayerImpl(P2, INITIAL_BALANCE);

        final Suit p1Suit2 = p1Card ? Suit.CLUBS : Suit.HEARTS;
        final int p1Count = pCardsCount.getOrDefault(P1, 2);
        for (int i = 0; i < p1Count; i++) {
            p1.getHand().addCard(new StandardCard(Rank.TWO, i == 0 ? Suit.CLUBS : p1Suit2));
        }
        final int p2Count = pCardsCount.getOrDefault(P2, 2);
        for (int i = 0; i < p2Count; i++) {
            p2.getHand().addCard(new StandardCard(Rank.TWO, i == 0 ? Suit.CLUBS : p1Suit2));
        }

        final List<Partecipant> player = List.of(p1, p2);

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
                        final List<Card> cards = new ArrayList<>();
                        for (int i = 0; i < dCardsCount; i++) {
                            cards.add(new StandardCard(Rank.TWO, Suit.SPADES));
                        }
                        return cards;
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
                return player;
            }

            @Override
            public void stand() {
            }

            @Override
            public Partecipant getCurrentPlayer() {
                return null;
            }

            @Override
            public int currentScore(final Hand hand) {
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

            @Override
            public void hit() {
            }

            @Override
            public boolean isGameOver() {
                return false;
            }

            @Override
            public void resetGame() {
            }

            @Override
            public void initialCards() {
            }

            @Override
            public SpecialRound getSpecialRound() {
                return null;
            }

            @Override
            public void initialCardsDealer() {
            }

        };
    }

}
