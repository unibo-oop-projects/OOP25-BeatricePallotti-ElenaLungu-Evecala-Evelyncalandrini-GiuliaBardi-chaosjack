package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import it.unibo.chaosjack.model.api.Wallet;
import it.unibo.chaosjack.model.impl.StandardWallet;

class WalletTest {
    private static final int INITIAL_BALANCE = 100;
    private static final int ADD_AMOUNT = 50;
    private static final int AFTER_ADD_BALANCE = 150;
    private static final int REMOVE_AMOUNT_OK = 40;
    private static final int AFTER_REMOVE_BALANCE = 60;
    private static final int REMOVE_AMOUNT_FAIL = 100;

    @Test
    void testInitialBalance() {
        final Wallet wallet = new StandardWallet(INITIAL_BALANCE);
        assertEquals(INITIAL_BALANCE, wallet.getBalance());
    }

    @Test
    void testAddFunds() {
        final Wallet wallet = new StandardWallet(INITIAL_BALANCE);
        wallet.addFunds(ADD_AMOUNT);
        assertEquals(AFTER_ADD_BALANCE, wallet.getBalance());
    }

    @Test
    void testRemoveFunds() {
        final Wallet wallet = new StandardWallet(INITIAL_BALANCE);
        assertTrue(wallet.removeFunds(REMOVE_AMOUNT_OK));
        assertEquals(AFTER_REMOVE_BALANCE, wallet.getBalance());

        assertFalse(wallet.removeFunds(REMOVE_AMOUNT_FAIL));
        assertEquals(AFTER_REMOVE_BALANCE, wallet.getBalance());
    }
}

