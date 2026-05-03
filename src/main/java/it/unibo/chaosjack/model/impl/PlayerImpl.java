package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.*; 
/**
 * This class implements a generic human player
 * Manages the player's financial state through a {@link Wallet}
 * Tracks the current bet
 */
public class PlayerImpl extends BasePlayer implements Player {  
    
    private final Wallet wallet;
    private  int currentBet;

    /**
     * Constructs a new human player with a specific name and  initial funds
     * @param name
     * @param initialFunds
     */
    public PlayerImpl(final String name,final int initialFunds){ //this is the constructor
        super(name); //lo eredito
        this.wallet = new StandardWallet(initialFunds);
        this.currentBet = 0; //quando inizio ancora non ho ancora nessuna bet
            
    }

    /**
     * Sets the bet with an assigned amount
     */
    @Override
    public void setBet(int amount) {
        this.currentBet = amount;
    }

    /**
     * @return the amount of funds of the player
     */
    @Override
    public int getWallet() {
        return this.wallet.getBalance(); 
    }

    /**
     * Updates the player's wallet balance
     * @param  amount to add or subtract
     * @return true if the operation was successfull
     */
    @Override
    public boolean updateWallet(int amount) {
        if( amount > 0){
            this.wallet.addFunds(amount);
            return true;
        }
        else if ( amount <0 ){
            return this.wallet.removeFunds(Math.abs(amount));
        }
        return true; 
    }

    /**
     * Gets the current bet
     */
    @Override
    public int getCurrentBet(){
        return this.currentBet;
    }

    /**
     * Executes a Double Down action.
     * Subtracts the current bet amount from the wallet one more time 
     * and doubles the value of the bet on the table.
     */
    @Override
    public void doubleDown(){
        this.updateWallet(-this.currentBet);
        this.currentBet *= 2; 
    }
}
