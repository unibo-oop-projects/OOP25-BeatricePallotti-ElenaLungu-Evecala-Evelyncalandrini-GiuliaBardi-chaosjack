package it.unibo.chaosjack.model.api;

import java.util.List;

/**
 * Rapresents the game table for the players and manages the match prograssion.
 */
public interface Table {

    /**
     * Rapresents the sequential game phases managed by the table.
     */
    enum State {
        FIRST_BET,
        PLAYING,
        FINAL_BET,
        DEALER_TURN,
        RESULTS;

        /**
         * Advances to the next logical state of the game.
         * 
         * @return the next state.
         * @throws IllegalStateException if the game has already ended (current state is RESULTS)
         */
        public State next() {
            return switch (this) {
                case FIRST_BET -> PLAYING;
                case PLAYING -> FINAL_BET;
                case FINAL_BET -> DEALER_TURN;
                case DEALER_TURN -> RESULTS;
                case RESULTS -> throw new IllegalStateException("The game is end");
            };
        }
    }

    /**
     * Returns the current status of the table.
     * 
     * @return current status of the table.
     */
    State getCurrentState();

    /**
     * Closes one phase to start another.
     * 
     * @throws IllegalStateException if the pot is empty o if the phase is wrong. 
     */
    void stepPassage();

    /**
     * Reset the table and pot.
     */
    void reset();

    /**
     * Reset for playing an other game.
     */
    void otherGame();

    /**
     * Returns the names of all the players currently playing at the table.
     * 
     * @return list of name's player gaming.
     */
    List<String> getPlayers();

    /**
     * Allows you to place a bet on the table.
     * 
     * @param playerName is the player's name.
     * @param amount the amount of chips to add to the pot.
     */
    void placeBet(String playerName, int amount);

    /**
     * Returns the total amount of fiches currently present in the table's pot.
     * 
     * @return total fishes on the table.
     */
    int getPot();

    /**
     * Evaluates the round and determines the winner.
     * 
     * @return winner evaluation of the round.
     */
    RoundEvaluation getWinner();

    /**
     * Returns the current total score of a specific player.
     * 
     * @param playerName is the player's name.
     * @return player's current score.
     */
    int getPlayerScore(String playerName);

    /**
     * Returns the current total score of the dealer.
     * 
     * @return dealer's current score.
     */
    int getDealerScore();

    /**
     * Returns the current balance available in the player's wallet.
     * 
     * @param playerName is the player's name.
     * @return the current balance in the player's wallet.
     */
    int getWalletBalance(String playerName);

    /**
     * Returns the overall statistics for each player involved in the game.
     * 
     * @return return the statistics for each player.
     */
    Statistics getStatistics();

}
