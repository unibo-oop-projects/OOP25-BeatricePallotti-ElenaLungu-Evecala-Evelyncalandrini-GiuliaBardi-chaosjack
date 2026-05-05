package it.unibo.chaosjack.model.api;

public interface Wallet {

    int getBalance();
    
    void addFunds(int amount);
    
    /**
     * Removes founds from wallet.
     * Returns false if the player doesn't have enough money
     */
    boolean removeFunds(int amount);
}
