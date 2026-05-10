package it.unibo.chaosjack.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.api.RoundResult.Outcome;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.Wallet;
import it.unibo.chaosjack.model.api.Statistics;

/**
 * Implementation of the Table interface.
 */
public final class TableImpl implements Table {
    private static final int MAX_SCORE = 21;
    private State currentState;
    private final Map<String, Integer> playerPots = new HashMap<>();
    private final Statistics statistics = new StatisticsImpl();
    private final List<String> players = new ArrayList<>();
    private final GameEngine engine;
    private final Wallet wallet;

    /**
     * Constructs a new TableImpl with the specified wallet, playerName and engine.
     * 
     * @param wallet wallet the player's starting wallet
     * @param playerName the name of the player
     * @param engine the game engine instance
     */
    public TableImpl(final Wallet wallet, final List<String> playerName, final GameEngine engine) { 
        this.wallet = wallet;
        this.players.addAll(playerName);
        this.engine = engine;
        this.reset();
    }

    @Override
    public State getCurrentState() {
        return this.currentState;
    }

    @Override
    public void stepPassage() {
        if (this.currentState == State.FIRST_BET && getPot() > 0) {
            this.currentState = State.PLAYING;
        } else if (this.currentState == State.PLAYING) {
            this.currentState = State.FINAL_BET;
        } else if (this.currentState == State.FINAL_BET) {
            this.currentState = State.DEALER_TURN;
        } else if (this.currentState == State.DEALER_TURN) {
            this.currentState = State.RESULTS;
        } else {
            throw new IllegalStateException("impossible step the phases");
        }
    }

    @Override
    public void otherGame() {
        this.playerPots.clear();
        this.currentState = State.FIRST_BET;
        this.statistics.incrementTotalRound();
    }

    @Override
    public void reset() {
        this.playerPots.clear();
        this.statistics.resetStats();
        this.currentState = State.FIRST_BET;
        this.statistics.incrementTotalRound();
    }

    @Override
    public List<String> getPlayers() {
        return List.copyOf(this.players);
    }

    @Override
    public void placeBet(final String playerName, final int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("The amount must be positive");
        }
        if (!(currentState == State.FIRST_BET || currentState == State.FINAL_BET)) {
            throw new IllegalStateException("Betting is not allowed during the " + currentState + " phase");
        }
        for (final String name : players) {
            if (name.equals(playerName)) {
                if (!wallet.removeFunds(amount)) {
                    throw new IllegalArgumentException("insufficient founds");
                }
                final int currentPot = playerPots.getOrDefault(playerName, 0);
                playerPots.put(playerName, currentPot + amount);
            }
        }

        if (haveAllPlayersBet()) {
            this.stepPassage();
        }
    }

    @Override
    public int getPot() {
        return playerPots.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public RoundResult getWinner() {
        final int dealerScore = getDealerScore();
        final List<String> bestPlayers = new ArrayList<>();
        int max = -1;

        for (final String name : getPlayers()) {
            final int score = getPlayerScore(name);
            if (score <= MAX_SCORE) {
                if (score > max) {
                    max = score;
                    bestPlayers.clear();
                    bestPlayers.add(name);
                } else if (score == max && max != -1) {
                    bestPlayers.add(name);
                }
            }
        }

        final RoundResult result;

        if (bestPlayers.isEmpty() || dealerScore <= MAX_SCORE && dealerScore > max) {
            result = new RoundResult(Outcome.DEALER_WON, max == -1 ? 0 : max, dealerScore, 0);
        } else if (max == dealerScore) {
            result = new RoundResult(Outcome.PUSH, max, dealerScore, 0);
        } else if (bestPlayers.size() > 1) {
                result = new RoundResult(Outcome.PLAYERS_PUSH, max, dealerScore, getPot() * 2);
        } else {
            final String oneWinner = bestPlayers.get(0);
            final HandImpl winnerHand = engine.getPlayers().stream()
                .filter(p -> p.getName().equals(oneWinner))
                .findFirst()
                .get()
                .getHand();

            final boolean isMonocolor = winnerHand.sameColor(winnerHand.getCards());
            final int bonus = isMonocolor ? 3 : 2;
            if (max == MAX_SCORE && isMonocolor) {
                result = new RoundResult(Outcome.BLACKJACK_BONUS, max, dealerScore, getPot() * (bonus + 2));
            } else if (max == MAX_SCORE) {
                result = new RoundResult(Outcome.PLAYER_BLACKJACK, max, dealerScore, getPot() * 3);
            } else if (isMonocolor) {
                result = new RoundResult(Outcome.PLAYER_BONUS, max, dealerScore, getPot() * bonus);
            } else {
                result = new RoundResult(Outcome.PLAYER_WON, max, dealerScore, getPot() * bonus);
            }
        }

        for (final String name : getPlayers()) {
            final int bet = playerPots.getOrDefault(name, 0);
            if (bestPlayers.contains(name)) {
                statistics.updateStats(name, result, bet);
            } else {
                final RoundResult individuaLoss = new RoundResult(Outcome.DEALER_WON, getPlayerScore(name), dealerScore, 0);
                statistics.updateStats(name, individuaLoss, bet);
            }
        }
        return result;
    }

    @Override
    public int getPlayerScore(final String playerName) {
        return engine.getPlayerScore(playerName);
    }

    @Override
    public int getDealerScore() {
        return engine.getDealerHand().getScore();
    }

    @Override
    public int getWalletBalance(final String playerName) {
        return wallet.getBalance();
    }

    @Override
    public Statistics geStatistics() {
        return this.statistics;
    }

    private boolean haveAllPlayersBet() {
        return players.stream().allMatch(playerPots::containsKey);
    }

}
