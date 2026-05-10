package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.api.Statistics;
import it.unibo.chaosjack.model.api.RoundResult.Outcome;
import it.unibo.chaosjack.model.impl.StatisticsImpl;

/**
 * Test for method of StatisticsImpl.
 */
class StatisticsTest {
    private static final int BET = 100;
    private static final int INITIAL_COUNT = 0;
    private static final int SINGLE_INCREMENT = 1;
    private static final int WIN_INCREMENT = 3;

    private static final int PAYOUT_WIN = 200;
    private static final int PAYOUT_BONUS = 400;
    private static final int PAYOUT_PUSH_MULTIPLE = 400;
    private static final int PAYOUT_ZERO = 0;

    private static final int EXPECTED_PROFIT_WIN = 100;
    private static final int EXPECTED_LOSS = -100;
    private static final int EXPECTED_LOSS_NEGATIVE = -200;
    private static final int TOTAL_PROFIT = 500;

    private static final int SCORE_20 = 20;
    private static final int SCORE_21 = 21;
    private static final int SCORE_19 = 19;
    private static final int SCORE_18 = 18;

    private static final int ROUNDS_TWO = 2;
    private static final int ROUNDS_ZERO = 0;

    private static final String P1 = "Marameo";
    private static final String P2 = "Bob";
    private static final String PI = "PlayerInesistente";

    private Statistics stats;

    @BeforeEach
    void setUp() {
        stats = new StatisticsImpl();
    }

    @Test
    void testWinning() {
        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_WON, SCORE_20, SCORE_19, PAYOUT_WIN), BET);
        assertEquals(SINGLE_INCREMENT, stats.getWinHistory().getOrDefault(P1, 0));
        assertEquals(EXPECTED_PROFIT_WIN, stats.getNetProfit().get(P1));

        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_BONUS, SCORE_20, SCORE_19, PAYOUT_BONUS), BET);
        assertEquals(SINGLE_INCREMENT, stats.getBonusHistory().getOrDefault(P1, 0));
        assertEquals(PAYOUT_BONUS, stats.getNetProfit().get(P1));

        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_BLACKJACK, SCORE_21, SCORE_19, PAYOUT_WIN), BET);
        assertEquals(SINGLE_INCREMENT, stats.getBlackHistory().getOrDefault(P1, 0));
        assertEquals(TOTAL_PROFIT, stats.getNetProfit().get(P1));

        stats.updateStats(P2, new RoundResult(Outcome.BLACKJACK_BONUS, SCORE_21, SCORE_19, PAYOUT_WIN), BET);
        assertEquals(SINGLE_INCREMENT, stats.getBlackBonusHistory().getOrDefault(P2, 0));
        assertEquals(EXPECTED_PROFIT_WIN, stats.getNetProfit().get(P2));
    }

    @Test
    void testPlayersPush() {
        final RoundResult pushResult = new RoundResult(Outcome.PLAYERS_PUSH, SCORE_20, SCORE_18, PAYOUT_PUSH_MULTIPLE);
        stats.updateStats(P1, pushResult, BET);
        stats.updateStats(P2, pushResult, BET);

        assertEquals(SINGLE_INCREMENT, stats.getPushHistory().getOrDefault(P1, 0));
        assertEquals(SINGLE_INCREMENT, stats.getPushHistory().getOrDefault(P2, 0));

        assertEquals(EXPECTED_PROFIT_WIN, stats.getNetProfit().get(P1));
        assertEquals(EXPECTED_PROFIT_WIN, stats.getNetProfit().get(P2));
    }

    @Test
    void testPushWithDealer() {
        stats.updateStats(P1, new RoundResult(Outcome.PUSH, SCORE_20, SCORE_20, PAYOUT_ZERO), BET);

        assertEquals(SINGLE_INCREMENT, stats.getLossHistory().getOrDefault(P1, 0));
        assertEquals(EXPECTED_LOSS, stats.getNetProfit().get(P1));
    }

    @Test
    void testWinsConsistency() {
        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_WON, SCORE_20, SCORE_19, PAYOUT_WIN), BET);
        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_WON, SCORE_21, SCORE_19, PAYOUT_WIN), BET);
        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_WON, SCORE_20, SCORE_19, PAYOUT_WIN), BET);

        assertEquals(WIN_INCREMENT, stats.getWinHistory().getOrDefault(P1, 0));
    }

    @Test
    void testDealerWon() {
        stats.updateStats(P1, new RoundResult(Outcome.DEALER_WON, SCORE_18, SCORE_20, PAYOUT_ZERO), BET);
        assertEquals(SINGLE_INCREMENT, stats.getLossHistory().getOrDefault(P1, 0));
        assertEquals(EXPECTED_LOSS, stats.getNetProfit().get(P1));
    }

    @Test
    void testNegativeProfitAccumulation() {
        stats.updateStats(P1, new RoundResult(Outcome.DEALER_WON, SCORE_18, SCORE_20, PAYOUT_ZERO), BET);
        stats.updateStats(P1, new RoundResult(Outcome.PUSH, SCORE_20, SCORE_20, PAYOUT_ZERO), BET);

        assertEquals(EXPECTED_LOSS_NEGATIVE, stats.getNetProfit().get(P1));
    }

    @Test
    void testEmptyPlayerStats() {
        assertEquals(INITIAL_COUNT, stats.getWinHistory().getOrDefault(PI, 0));
        assertNull(stats.getNetProfit().get(PI));
    }

    @Test
    void testProfitFluctuation() {
        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_WON, SCORE_20, SCORE_18, PAYOUT_WIN), BET);
        stats.updateStats(P1, new RoundResult(Outcome.DEALER_WON, SCORE_18, SCORE_20, PAYOUT_ZERO), BET);
        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_WON, SCORE_20, SCORE_18, PAYOUT_WIN), BET);

        assertEquals(EXPECTED_PROFIT_WIN, stats.getNetProfit().get(P1));
    }

    @Test
    void testRoundAndReset() {
        stats.incrementTotalRound();
        stats.incrementTotalRound();
        assertEquals(ROUNDS_TWO, stats.getTotalRounds());

        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_WON, SCORE_20, SCORE_18, PAYOUT_WIN), BET);
        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_WON, SCORE_20, SCORE_18, PAYOUT_WIN), BET);
        stats.resetStats();

        assertEquals(ROUNDS_ZERO, stats.getTotalRounds());
        assertTrue(stats.getWinHistory().isEmpty());
        assertTrue(stats.getNetProfit().isEmpty());
    }

}
