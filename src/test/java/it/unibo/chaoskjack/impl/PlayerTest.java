package it.unibo.chaoskjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.impl.PlayerImpl;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.StandardCard;
import it.unibo.chaosjack.model.impl.Suit;

 class PlayerTest { 
    
    @Test
    void testWalletUpdates() {
        final int initialFunds = 100;
        final Player player = new PlayerImpl("Giulia",initialFunds);
        player.updateWallet(50);
        assertEquals(150, player.getWallet(), "Il saldo dovrebbe salire a 150");
        final boolean success = player.updateWallet(-100);
        assertTrue(success, "L'operazione dovrebbe riuscire");
        assertEquals(50, player.getWallet(), "Il saldo dovrebbe scendere a 50");

    }

    @Test
    void testSetBet() {
        final int initialFunds = 100;
        final Player player = new PlayerImpl("Franco", initialFunds);
        player.setBet(30);
        assertEquals(30, player.getCurrentBet(),"La scommessa dovrebbe essere di 30");
    }

    @Test 
    void testSetBetInvalidAmounts() {
    
    final int initialFunds = 100;
    final Player player = new PlayerImpl("Giulia", initialFunds);
    player.setBet(50);
    assertEquals(50, player.getCurrentBet(), "La scommessa dovrebbe essere 50");
    assertThrows(IllegalArgumentException.class, () -> {
        player.setBet(200); 
    }, "Dovrebbe lanciare un'eccezione se la scommessa supera i fondi");
    assertThrows(IllegalArgumentException.class, () -> {
        player.setBet(0);
    }, "Dovrebbe lanciare un'eccezione per scommessa uguale a zero");

    assertThrows(IllegalArgumentException.class, () -> {
        player.setBet(-10);
    }, "Dovrebbe lanciare un'eccezione per scommessa negativa");
   }

   @Test
   void testDoubleDown() {
      final int initialBet = 20;
      final int initialFunds = 100;
      final Player player = new PlayerImpl("Andrea", initialFunds);
      player.setBet(initialBet);
      player.doubleDown();
      assertEquals(40, player.getCurrentBet(), "La scommessa dovrebbe essere 40");
      assertEquals(80, player.getWallet(), "Il portafoglio dovrebbe avere 80");
   }

   @Test
   void standardScore() {
    final int initialFunds = 100;
    final Player player = new PlayerImpl("Paolo", initialFunds);
    player.addCard(new StandardCard(Rank.SEVEN, Suit.CLUBS));
    player.addCard(new StandardCard(Rank.EIGHT, Suit.DIAMONDS));
    assertEquals(15, player.getHand().getScore(), "Il punteggio dovrebbe essere 15");
    player.addCard(new StandardCard(Rank.KING, Suit.SPADES));
    assertEquals(25, player.getHand().getScore(), "Il punteggio dovrebbe essere 25");
   }

   @Test
   void testScoreWithAces() {
    final int initialFunds = 100;
    final Player player = new PlayerImpl("Emanuele", initialFunds);
    player.addCard(new StandardCard(Rank.ACE, Suit.DIAMONDS));
    player.addCard(new StandardCard(Rank.JACK, Suit.DIAMONDS));
    assertEquals(21, player.getHand().getScore(), "Il punteggio dovrebbe essere 21"); 
    player.addCard(new StandardCard(Rank.FIVE, Suit.HEARTS));
    assertEquals(16, player.getHand().getScore(), "Il punteggio dovrebbe essere 16");
   }
}

