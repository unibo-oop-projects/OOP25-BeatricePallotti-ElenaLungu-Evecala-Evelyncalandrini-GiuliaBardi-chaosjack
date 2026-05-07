package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.api.Wallet;

/**
 * This class implements a generic human player.
 * Manages the player's financial state through a {@link Wallet}
 * Tracks the current bet
 */

public class PlayerImpl extends AbstractPlayer implements Player { 

    private final Wallet wallet;
    private int currentBet;

    /**
     * Constructs a new human player with a specific name and  initial funds.
     * 
     * @param name The name of the player
     * @param initialFunds The initial amount of money for the player
     */

    public PlayerImpl(final String name, final int initialFunds) { 
        super(name); 
        this.wallet = new StandardWallet(initialFunds);
        this.currentBet = 0;
    }

    @Override
    public void setBet(final int amount) {
        if (amount > 0 && amount <= this.getWallet()) {
            this.currentBet = amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds for the bet");
        }
    }

    @Override
    public final int getWallet() {
        return this.wallet.getBalance(); 
    }

    @Override
    public final boolean updateWallet(final int amount) {
        if (amount > 0) {
            this.wallet.addFunds(amount);
            return true;
        } else if (amount < 0) {
            return this.wallet.removeFunds(Math.abs(amount));
        }
        return true; 
    }

    @Override
    public final int getCurrentBet() {
        return this.currentBet;
    }

    @Override
    public final void doubleDown() {
        this.updateWallet(-this.currentBet);
        this.currentBet *= 2; 
    }
}
