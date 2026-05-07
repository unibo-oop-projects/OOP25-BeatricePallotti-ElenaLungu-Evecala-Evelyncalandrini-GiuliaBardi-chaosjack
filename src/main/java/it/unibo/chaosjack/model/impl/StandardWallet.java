package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.Wallet;

/**
 * Standard implementation of the Wallet interface.
 */
public final class StandardWallet implements Wallet {

    private int balance;

    /**
     * Constructs a new StandardWallet with an initial balance.
     *
     * @param initialBalance the starting amount of money
     */
    public StandardWallet(final int initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("It's not possible to play if you are in debt");
        }
        this.balance = initialBalance;
    }

    @Override
    public int getBalance() {
        return this.balance;
    }

    @Override
    public void addFunds(final int amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    @Override
    public boolean removeFunds(final int amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }
}
