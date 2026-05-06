package it.unibo.chaosjack.model.api;

/**
 * Interface representing a player's wallet to manage betting funds.
 */
public interface Wallet {

    /**
     * Gets the current balance of the wallet.
     * 
     * @return the current amount of money
     */
    int getBalance();

    /**
     * Adds funds to the wallet.
     * 
     * @param amount the amount of money to add
     */
    void addFunds(int amount);

    /**
     * Attempts to remove funds from the wallet.
     * 
     * @param amount the amount of money to remove
     * @return true if the funds were successfully removed, false if the balance is insufficient
     */
    boolean removeFunds(int amount);
}
