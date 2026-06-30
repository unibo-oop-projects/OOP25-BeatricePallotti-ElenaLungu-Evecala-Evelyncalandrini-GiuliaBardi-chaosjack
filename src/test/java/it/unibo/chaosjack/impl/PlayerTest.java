package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.impl.PlayerImpl;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.StandardCard;
import it.unibo.chaosjack.model.impl.Suit;

 /**
  * Tests for class PlayerImpl.
  */
 class PlayerTest { 

    @Test
    void testWalletUpdates() {
        final int initialFunds = 100;
        final int amount = 50;
        final int expectedFunds = 150;
        final int secondAmount = -100;
        final int expectedResult = 50;
        final Player player = new PlayerImpl("Giulia", initialFunds);
        player.updateWallet(amount);
        assertEquals(expectedFunds, player.getWallet(), "Il saldo dovrebbe salire a 150");
        final boolean success = player.updateWallet(secondAmount);
        assertTrue(success, "L'operazione dovrebbe riuscire");
        assertEquals(expectedResult, player.getWallet(), "Il saldo dovrebbe scendere a 50");

    }

    @Test
    void testSetBet() {
        final int initialFunds = 100;
        final int betAmount = 30;
        final Player player = new PlayerImpl("Franco", initialFunds);
        player.setBet(betAmount);
        assertEquals(betAmount, player.getCurrentBet(), "La scommessa dovrebbe essere di 30");
    }

    @Test 
    void testSetBetInvalidAmounts() {
    final int initialFunds = 100;
    final int validBet = 50;
    final int tooHighBet = 200;
    final int negativeBet = -10;
    final Player player = new PlayerImpl("Giulia", initialFunds);
    player.setBet(validBet);
    assertEquals(validBet, player.getCurrentBet(), "La scommessa dovrebbe essere 50");
    assertThrows(IllegalArgumentException.class, () -> {
        player.setBet(tooHighBet); 
    }, "Dovrebbe lanciare un'eccezione se la scommessa supera i fondi");
    assertThrows(IllegalArgumentException.class, () -> {
        player.setBet(0);
    }, "Dovrebbe lanciare un'eccezione per scommessa uguale a zero");

    assertThrows(IllegalArgumentException.class, () -> {
        player.setBet(negativeBet);
    }, "Dovrebbe lanciare un'eccezione per scommessa negativa");
   }

   @Test
   void testDoubleDown() {
      final int initialBet = 20;
      final int initialFunds = 100;
      final int expectedBet = 40;
      final int expectedFunds = 60;
      final Player player = new PlayerImpl("Andrea", initialFunds);
      player.setBet(initialBet);
      player.doubleDown();
      assertEquals(expectedBet, player.getCurrentBet(), "La scommessa dovrebbe essere 40");
      assertEquals(expectedFunds, player.getWallet(), "Il portafoglio dovrebbe avere 60");
   }

   @Test
   void standardScore() {
    final int initialFunds = 100;
    final int expectedScore = 15;
    final int expectedSecondScore = 25;
    final Player player = new PlayerImpl("Paolo", initialFunds);
    player.addCard(new StandardCard(Rank.SEVEN, Suit.CLUBS));
    player.addCard(new StandardCard(Rank.EIGHT, Suit.DIAMONDS));
    assertEquals(expectedScore, player.getHand().getScore(), "Il punteggio dovrebbe essere 15");
    player.addCard(new StandardCard(Rank.KING, Suit.SPADES));
    assertEquals(expectedSecondScore, player.getHand().getScore(), "Il punteggio dovrebbe essere 25");
   }

   @Test
   void testScoreWithAces() {
    final int initialFunds = 100;
    final int expectedScore = 21;
    final int expectedSecondScore = 16;
    final Player player = new PlayerImpl("Emanuele", initialFunds);
    player.addCard(new StandardCard(Rank.ACE, Suit.DIAMONDS));
    player.addCard(new StandardCard(Rank.JACK, Suit.DIAMONDS));
    assertEquals(expectedScore, player.getHand().getScore(), "Il punteggio dovrebbe essere 21"); 
    player.addCard(new StandardCard(Rank.FIVE, Suit.HEARTS));
    assertEquals(expectedSecondScore, player.getHand().getScore(), "Il punteggio dovrebbe essere 16");
   }
}

