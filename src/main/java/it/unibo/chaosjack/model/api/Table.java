package it.unibo.chaosjack.model.api;

import java.util.List;


public interface Table {

    /**
     * Rapresents the game phases managed by the table
     */
    enum State{
        FIRST_BET,
        PLAYING,
        FINAL_BET,
        DEALER_TURN,
        RESULTS
    }

    /**
     * @return current status of the table
     */
    State getCurrentState();

    /**
     * Closes one phase to start another
     * @throws IllegalStateException if the pot is empty o if the phase is wrong 
     */
    void stepPassage();

    /**
     * Reset the table and pot
     */
    void reset();

    /**
     * Reset for playing an other game
     */
    void otherGame();

    /**
     * @return list of name's player gaming 
     */
    List<String> getPlayers();

    /**
     * Allows you to place a bet on the table.
     * @param amount the amount of chips to add to the pot
     */
    void placeBet(String playerName, int amount);

    /**
     * @return total fishes on the table
     */
    int getPot();
    
    /**
     * @return winner of the round
     */
    RoundResult getWinner();
    
    /**
     * @return player's current score 
     */
    int getPlayerScore(String playerName);

    /**
     * @return dealer's current score
     */
    int getDealerScore();

    /**
     * @return the current balance in the player's wallet
     */
    int getWalletBalance(String playerName);

    /**
     * @return return the statistics for each player
     */
    Statistics geStatistics();

}