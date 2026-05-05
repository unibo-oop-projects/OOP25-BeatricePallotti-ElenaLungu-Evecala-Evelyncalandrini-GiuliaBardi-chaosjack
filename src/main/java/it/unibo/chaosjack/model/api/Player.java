package it.unibo.chaosjack.model.api;

   /**
   * This interface represents the player in the Blackjack game where it can bet money.
   */
public interface Player extends Partecipant{ 

    /**
     * Sets the amount of money the player wants to wager on the current hand.
     * 
     * @param amount
     */
    void setBet(int amount);

    /**
     * Retrieves the current balance available in the player's wallet.
     * 
     * @return the wallet
     */
    int getWallet();

    /**
     * Updates the player's wallet balance by adding or subtracting a specific amount.
     * 
     * @param amount
     * @return the updated wallet
     */
    boolean updateWallet(int amount);

    /**
     * @return the current amount of the bet.
     */
    int getCurrentBet();

    /**
     * It doubles the amount of the current bet.
     */
    void doubleDown(); 

}
