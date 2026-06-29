package it.unibo.chaosjack.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.api.RoundEvaluation;
import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.api.RoundResult.Outcome;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.Statistics;

/**
 * Implementation of the Table interface.
 */
public final class TableImpl implements Table {

    private State currentState;
    private final Map<String, Integer> playerPots = new HashMap<>();
    private final Statistics statistics = new StatisticsImpl();
    private final List<String> players = new ArrayList<>();
    private final GameEngine engine;
    private final RoundEvaluator evaluator = new RoundEvaluator();
    private boolean doUpdate = true;

    /**
     * Constructs a new TableImpl with the specified playerName and engine.
     * 
     * @param playerName the name of the player
     * @param engine the game engine instance
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "The GameEngine is a core architectural componet shared between table and controller."
    )
    public TableImpl(final List<String> playerName, final GameEngine engine) { 
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
       if (this.currentState == State.FIRST_BET && getPot() <= 0) {
            throw new IllegalStateException();
       }

       this.currentState = this.currentState.next();
    }

    @Override
    public void otherGame() {
        this.playerPots.clear();
        this.currentState = State.FIRST_BET;
        this.statistics.incrementTotalRound();
        this.doUpdate = true;
    }

    @Override
    public void reset() {
        this.playerPots.clear();
        this.statistics.resetStats();
        this.currentState = State.FIRST_BET;
        this.doUpdate = true;
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

        if (!(currentState == State.FIRST_BET || currentState == State.FINAL_BET || currentState == State.PLAYING)) {
            throw new IllegalStateException("Betting is not allowed during the " + currentState + " phase");
        }

        if (!players.contains(playerName)) {
          throw new IllegalArgumentException("Player not found at the table" + playerName); 
        }

        playerPots.merge(playerName, amount, Integer::sum);
    }

    @Override
    public int getPot() {
        return playerPots.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public RoundEvaluation getWinner() {
        final RoundEvaluation evaluation = evaluator.evaluate(this.engine, getPlayers(), getPot());
        if (this.doUpdate) {
            updatePlayersStatistics(evaluation.winners(), evaluation.result(), getDealerScore());
            this.doUpdate = false;
        }

        return evaluation;
    }

    private void updatePlayersStatistics(final List<String> bestPlayer, final RoundResult roundResult, final int dealerScore) {
        for (final String name : getPlayers()) {
            final int bet = playerPots.getOrDefault(name, 0);
            if (bestPlayer.contains(name)) {
                statistics.updateStats(name, roundResult, bet);
                engine.getPlayers().stream()
                    .filter(p -> p.getName().equals(name))
                    .map(p -> (Player) p)
                    .findFirst()
                    .ifPresent(player -> player.updateWallet(roundResult.getPayOut()));
            } else {
                final RoundResult individuaLoss = new RoundResult(Outcome.DEALER_WON, getPlayerScore(name), dealerScore, 0);
                statistics.updateStats(name, individuaLoss, bet);
            }
        }
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
        return engine.getPlayers().stream()
            .filter(p -> p.getName().equals(playerName))
            .findFirst()
            .map(p -> ((Player) p).getWallet())
            .orElse(0);
    }

    @Override
    public Statistics getStatistics() {
        return this.statistics;
    }

}
