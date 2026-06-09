package it.unibo.chaosjack.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.Hand;
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
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "The GameEngine is a core architectural componet shared between table and controller."
    )
    public TableImpl(final Wallet wallet, final List<String> playerName, final GameEngine engine) { 
        this.wallet = new StandardWallet(wallet.getBalance());
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
       if (this.currentState == State.FIRST_BET && getPot() <= 0){
            throw new IllegalStateException();
       }

       this.currentState = this.currentState.next();
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
        /* 
        final var player = engine.getPlayers().stream()
            .filter(p -> p.getName().equals(playerName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        try {
            player.setBet(amount);
            this.wallet.addFunds(amount);

            final int currentPot = playerPots.getOrDefault(playerName, 0);
            playerPots.put(playerName, currentPot + amount);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Insufficient founds to place this bet");
        }
        /*if(!players.contains(playerName)) {
            throw new IllegalArgumentException("Player non found at the table: " + playerName);
        }*/
        for (final String name : players) {
            if (name.equals(playerName)) {
                if (!wallet.removeFunds(amount)) {
                    throw new IllegalArgumentException("insufficient founds");
                }
                final int currentPot = playerPots.getOrDefault(playerName, 0);
                playerPots.put(playerName, currentPot + amount);
            }
        }
        //wallet.addFunds(amount);
        /*inal int currentPot = playerPots.getOrDefault(playerName, 0);
        playerPots.put(playerName, currentPot + amount);*/
        
        /*if (haveAllPlayersBet()) {
            this.stepPassage();
        }*/
    }

    @Override
    public int getPot() {
        return playerPots.values().stream().mapToInt(Integer::intValue).sum();
        //return this.wallet.getBalance();
    }

    @Override
    public RoundResult getWinner() {
        final int dealerScore = getDealerScore();
        final int dealerCardsCount = engine.getDealerHand().getCards().size();

        int max = -1;
        int minCards = 1;
        final List<String> bestPlayers = new ArrayList<>();

        for (final String name : getPlayers()) {
            final int score = getPlayerScore(name);
            if (score <= MAX_SCORE) {
                final int cardsCount = getPlayerCardCount(name);
                if (score > max || (score == max && cardsCount < minCards)) {
                    max = score;
                    minCards = cardsCount;
                    bestPlayers.clear();
                    bestPlayers.add(name);
                } else if (score == max && cardsCount == minCards) {
                    bestPlayers.add(name);
                }
            }
        }

        final RoundResult result = calculateRoundResult(bestPlayers, max, minCards, dealerScore, dealerCardsCount);
        updatePlayersStatistics(bestPlayers, result, dealerScore);

        return result;

    }

    private int getPlayerCardCount(final String playerName) {
        return engine.getPlayers().stream()
            .filter(p -> p.getName().equals(playerName))
            .findFirst()
            .map(p -> p.getHand().getCards().size())
            .orElse(2);
    }

    private void updatePlayersStatistics(final List<String> bestPlayer, final RoundResult roundResult, final int dealerScore) {
        for (final String name : getPlayers()) {
            final int bet = playerPots.getOrDefault(name, 0);
            if (bestPlayer.contains(name)) {
                statistics.updateStats(name, roundResult, bet);
            } else {
                final RoundResult individuaLoss = new RoundResult(Outcome.DEALER_WON, getPlayerScore(name), dealerScore, 0);
                statistics.updateStats(name, individuaLoss, bet);
            }
        }
    } 

    private RoundResult calculateRoundResult (final List<String> bestPlayer, final int maxScore, final int minCards, final int dealerScore, final int dealerCardsCount) {
        if (bestPlayer.isEmpty()) {
            return new RoundResult(Outcome.DEALER_WON, 0, dealerScore, 0);
        }

        if (dealerScore <= MAX_SCORE) {
            if (dealerScore > maxScore) {
                return new RoundResult(Outcome.DEALER_WON, maxScore, dealerScore, 0);
            }

            if (dealerScore == maxScore) {
                if (dealerCardsCount < minCards) {
                    return new RoundResult(Outcome.DEALER_WON, maxScore, dealerScore, 0);
                }
                if (dealerCardsCount == minCards) {
                    return new RoundResult(Outcome.PUSH, maxScore, dealerScore, 0);
                }
            }
        }

        if (bestPlayer.size() > 1) {
            return new RoundResult(Outcome.PLAYERS_PUSH, maxScore, dealerScore, getPot() * 2);
        }

        return calculateSingleWinnerResult(bestPlayer.get(0), maxScore, dealerScore);
    }

    private RoundResult calculateSingleWinnerResult(final String winner, final int playerScore, final int dealerScore) {
        final Hand winnerHand = engine.getPlayers().stream()
                .filter(p -> p.getName().equals(winner))
                .findFirst()
                .get()
                .getHand();
        
        final boolean isMonocolor = winnerHand.sameColor(winnerHand.getCards());
        final int bonus = isMonocolor ? 3 : 2;
        final boolean isBlackjack = (playerScore == MAX_SCORE);
        final int pot = getPot();

        if (isBlackjack && isMonocolor) {
            return new RoundResult(Outcome.BLACKJACK_BONUS, playerScore, dealerScore, pot * (bonus + 2));
        }

        if (isBlackjack) {
            return new RoundResult(Outcome.PLAYER_BLACKJACK, playerScore, dealerScore, pot * 3);
        }

        if (isMonocolor) {
            return new RoundResult(Outcome.PLAYER_BONUS, playerScore, dealerScore, pot * bonus);
        }

        return new RoundResult(Outcome.PLAYER_WON, playerScore, dealerScore, pot * bonus);
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

}
