package it.unibo.chaosjack.model.impl;

import java.util.HashMap;
import java.util.Map;

import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.api.RoundResult.Outcome;
import it.unibo.chaosjack.model.api.Statistics;

/**
 * Implementation of interface Statistics.
 */
public final class StatisticsImpl implements Statistics {

    private int totalRounds;
    private final Map<String, Integer> wins = new HashMap<>();
    private final Map<String, Integer> black = new HashMap<>();
    private final Map<String, Integer> bonusWin = new HashMap<>();
    private final Map<String, Integer> bonusBlack = new HashMap<>();
    private final Map<String, Integer> losses = new HashMap<>();
    private final Map<String, Integer> pushes = new HashMap<>();
    private final Map<String, Integer> netProfit = new HashMap<>();

    @Override
    public void updateStats(final String playerName, final RoundResult result, final int betAmount) {
        final Outcome outcome = result.outcome();
        final int payOut = result.getPayOut();

        switch (outcome) {
            case PLAYER_WON, PLAYER_BLACKJACK, PLAYER_BONUS, BLACKJACK_BONUS -> {
                wins.put(playerName, wins.getOrDefault(playerName, 0) + 1);
                if (outcome == Outcome.PLAYER_BLACKJACK) {
                    black.put(playerName, black.getOrDefault(playerName, 0) + 1);
                } else if (outcome == Outcome.PLAYER_BONUS) {
                    bonusWin.put(playerName, bonusWin.getOrDefault(playerName, 0) + 1);
                } else if (outcome == Outcome.BLACKJACK_BONUS) {
                    bonusBlack.put(playerName, bonusBlack.getOrDefault(playerName, 0) + 1);
                }
                netProfit.put(playerName, netProfit.getOrDefault(playerName, 0) + payOut - betAmount);
            }
            case DEALER_WON, PUSH -> {
                losses.put(playerName, losses.getOrDefault(playerName, 0) + 1);
                netProfit.put(playerName, netProfit.getOrDefault(playerName, 0) - betAmount);
            }
            case PLAYERS_PUSH -> {
                pushes.put(playerName, pushes.getOrDefault(playerName, 0) + 1);
                netProfit.put(playerName, netProfit.getOrDefault(playerName, 0) + (payOut / 2) - betAmount);
            }

        }
    }

    @Override
    public int getTotalRounds() {
       return this.totalRounds;
    }

    @Override
    public Map<String, Integer> getWinHistory() {
        return new HashMap<>(wins);
    }

    @Override
    public Map<String, Integer> getBlackHistory() {
        return new HashMap<>(black);
    }

    @Override
    public Map<String, Integer> getBonusHistory() {
       return new HashMap<>(bonusWin);
    }

    @Override
    public Map<String, Integer> getBlackBonusHistory() {
       return new HashMap<>(bonusBlack);
    }

    @Override
    public Map<String, Integer> getLossHistory() {
        return new HashMap<>(losses);
    }

    @Override
    public Map<String, Integer> getPushHistory() {
        return new HashMap<>(pushes);
    }

    @Override
    public Map<String, Integer> getNetProfit() {
        return new HashMap<>(netProfit);
    }

    @Override
    public void incrementTotalRound() {
        this.totalRounds++;
    }

    @Override
    public void resetStats() {
        this.totalRounds = 0;
        wins.clear();
        losses.clear();
        pushes.clear();
        netProfit.clear();
    }
}
