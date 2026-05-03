package it.unibo.chaoskjack.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.api.Statistics;
import it.unibo.chaosjack.model.api.RoundResult.Outcome;
import it.unibo.chaosjack.model.impl.StatisticsImpl;

public class StatisticsTest {
    private Statistics stats;
    private static final String P1 = "Marameo";
    private static final String P2 = "Bob";
    private static final int BET = 100;

    @BeforeEach
    void setUp() {
        stats = new StatisticsImpl();
    }

    @Test
    void testWinning() {
        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_WON, 20, 19, 200), BET);
        assertEquals(1, stats.getWinHistory().getOrDefault(P1, 0));
        assertEquals(100, stats.getNetProfit().get(P1));

        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_BONUS, 20, 19, 400), BET);
        assertEquals(1, stats.getBonusHistory().getOrDefault(P1, 0));
        assertEquals(400, stats.getNetProfit().get(P1));

        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_BLACKJACK, 21, 19, 200), BET);
        assertEquals(1, stats.getBlackHistory().getOrDefault(P1, 0));
        assertEquals(500, stats.getNetProfit().get(P1));

        stats.updateStats(P2, new RoundResult(Outcome.BLACKJACK_BONUS, 21, 19, 200), BET);
        assertEquals(1, stats.getBlackBonusHistory().getOrDefault(P2, 0));
        assertEquals(100, stats.getNetProfit().get(P2));

    }          

    @Test
    void testPlayersPush() {
        RoundResult pushResult = new RoundResult(Outcome.PLAYERS_PUSH, 20, 18, 400);
        stats.updateStats(P1, pushResult, BET);
        stats.updateStats(P2, pushResult, BET);

        assertEquals(1, stats.getPushHistory().getOrDefault(P1, 0));
        assertEquals(1, stats.getPushHistory().getOrDefault(P2, 0));

        assertEquals(100, stats.getNetProfit().get(P1));
        assertEquals(100, stats.getNetProfit().get(P2));
    }

    @Test
    void testPushWithDealer() {
        stats.updateStats(P1, new RoundResult(Outcome.PUSH, 20, 20, 0), BET);

        assertEquals(1, stats.getLossHistory().getOrDefault(P1, 0));
        assertEquals(-100, stats.getNetProfit().get(P1));
    }

    @Test
    void testRoundAndReset() {
        stats.incrementTotalRound();
        stats.incrementTotalRound();
        assertEquals(2, stats.getTotalRounds());

        stats.updateStats(P1, new RoundResult(Outcome.PLAYER_WON, 20, 18, 200), BET);
        stats.resetStats();

        assertEquals(0, stats.getTotalRounds());
        assertTrue(stats.getWinHistory().isEmpty());
        assertTrue(stats.getNetProfit().isEmpty());
    }

}
