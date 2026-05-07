package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unibo.chaosjack.model.api.Wallet;
import it.unibo.chaosjack.model.impl.StandardWallet;

class WalletTest {

    @Test
    void testInitialBalance() {
        final Wallet wallet = new StandardWallet(100);
        assertEquals(100, wallet.getBalance());
    }

    @Test
    void testAddFunds() {
        final Wallet wallet = new StandardWallet(100);
        wallet.addFunds(50);
        assertEquals(150, wallet.getBalance());
    }

    @Test
    void testRemoveFunds() {
        final Wallet wallet = new StandardWallet(100);
        // Test prelievo possibile
        assertTrue(wallet.removeFunds(40));
        assertEquals(60, wallet.getBalance());
        
        // Test prelievo impossibile (troppo povera)
        assertFalse(wallet.removeFunds(100));
        assertEquals(60, wallet.getBalance()); // Il bilancio non deve cambiare
    }
}