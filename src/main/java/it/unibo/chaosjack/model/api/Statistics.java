package it.unibo.chaosjack.model.api;

import java.util.Map;

/**
 * Manages player statistics and scores
 */
public interface Statistics {

    /**
     * Records the result of a completed round
     * 
     * @param playerName name of the player
     * @param result of the round just finish
     * @param betAmount value of bet in the round for each player
     */
    void updateStats(String playerName, RoundResult result, int betAmount);

    /**
     * @return the total number of round played in the session
     */
    int getTotalRounds();

    /**
     * @return the number of wins for each player
     */
    Map<String, Integer> getWinHistory();

    /**
     * @return the number of BlackJack for each player
     */
    Map<String, Integer> getBlackHistory();

    /**
     * @return the number of wins with bonus for each player
     */
    Map<String, Integer> getBonusHistory();

    /**
     * @return the number of wins with BlackJack with bonus for each player
     */
    Map<String, Integer> getBlackBonusHistory();

    /**
     * @return the number of lose for each player
     */
    Map<String, Integer> getLossHistory();

    /**
     * @return the number of pushes with other player for each player
     */
    Map<String, Integer> getPushHistory();
    
    /**
     * @return the net profit (winnings - losses) for each player
     */
    Map<String, Integer> getNetProfit();

    /**
     * Increment the round for passege a new round
     */
    void incrementTotalRound();

    /**
     * reset of the statistics
     */
    void resetStats(); 
    
}
