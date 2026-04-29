package it.unibo.chaosjack.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.api.RoundResult.Outcome;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.Wallet;

public class TableImpl implements Table{
    private State currentState;
    private final Map<String, Integer> winCounters = new HashMap<>();
    private int roundCount;
    private final Map<String, Integer> playerPots = new HashMap<>();
    private final Map<String, Integer> playerWins = new HashMap<>();
    private final List<String> players = new ArrayList<>();
    //private final gameEngine engine;
    private final Wallet wallet;

    public TableImpl(Wallet wallet, List<String> playerName){ // gameEngine engine
        this.wallet = wallet;
        this.players.addAll(playerName);
        //this.roundCount = 0;
        // this.engine = engine
        this.reset();
    }

    @Override
    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void stepPassage() {
        if (this.currentState == State.FIRST_BET && getPot() > 0) {
            this.currentState = State.PLAYING;
            //engine.changeState(turnState.PLAYER_TURN);
        } else if (this.currentState == State.FINAL_BET) {
            this.currentState = State.DEALER_TURN;
            //engine.changeState(turnState.DEALER_TURN);
        } else {
            throw new IllegalStateException("impossible step the phases");
        }
    }

    @Override
    public void otherGame() {
        playerPots.clear();
        currentState = State.FIRST_BET;
        roundCount++;
    }

    @Override
    public void reset() {
        playerPots.clear();
        playerWins.clear();
        currentState = State.FIRST_BET;
        roundCount = 1;
    }

    @Override
    public List<String> getPlayers() {
        return List.copyOf(this.players);
    }

    @Override
    public void placeBet(final String playerName, final int amount) {
        if (amount <= 0 ) {
            throw new IllegalArgumentException("The amount must be positive");
        }
        if (currentState != State.FIRST_BET && currentState != State.FINAL_BET) {
            throw new IllegalStateException("Betting is not allowed during the " + currentState + " phase");
        }
        for (String name : players){
            if (name.equals(playerName)){
                if (wallet.removeFunds(amount) == false){
                    throw new IllegalArgumentException("insufficient founds");
                }
                int currentPot = playerPots.getOrDefault(playerName, 0);
                playerPots.put(playerName, currentPot + amount);
            }
        }
    }

    @Override
    public int getPot() {
        return playerPots.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public RoundResult getWinner() {
        final int dealerScore = getDealerScore();
        List<String> bestPlayers = new ArrayList<>();
        int max = -1;

        for (String name : getPlayers()){
            int score = getPlayerScore(name);
            if (score <= 21){
                if (score > max){
                    max = score;
                    bestPlayers.clear();
                    bestPlayers.add(name);
                } else if (score == max && max != -1) {
                    bestPlayers.add(name);
                }
            }
        }

        // Dealer win or nobody players is valid
        if (bestPlayers.isEmpty() || (dealerScore <= 21 && dealerScore > max)) {
            return new RoundResult(Outcome.DEALER_WON, max == -1 ? 0 : max, dealerScore, 0);
        }
        // Push
        if (max == dealerScore) {
            return new RoundResult(Outcome.PUSH, max, dealerScore,0);
        } else {
            for (String winnerName : bestPlayers) {
                winCounters.put(winnerName, winCounters.getOrDefault(winnerName, 0) + 1);
            }

            if (bestPlayers.size() > 1){
                return new RoundResult(Outcome.PLAYERS_PUSH, max, dealerScore, getPot()*2);
            } else {
            /*if (hand.isMonocolor(bestPlayer)){
                return new RoundResult(Outcome.PLAYER_WON, max, dealerScore, getPot()*3);
            } else {*/
                return new RoundResult(Outcome.PLAYER_WON, max, dealerScore, getPot()/*2*/);
            }
            //}
        }
    }

    @Override
    public int getPlayerScore(String playerName) {
        return 0;
        // return engine.getPlayerHand().getScore();
    }

    @Override
    public int getDealerScore() {
        return 0;
        //return engine.getDealerHand().getScore();
    }

    @Override
    public int getWalletBalance(String playerName) {
        return wallet.getBalance(); //wallet.getBalance(playerName)
    }

    @Override
    public int getRoundCount() {
        return roundCount;
    }

    @Override
    public int getWinsCount(String playerName) {
        return winCounters.getOrDefault(playerName, 0);
    }
}
