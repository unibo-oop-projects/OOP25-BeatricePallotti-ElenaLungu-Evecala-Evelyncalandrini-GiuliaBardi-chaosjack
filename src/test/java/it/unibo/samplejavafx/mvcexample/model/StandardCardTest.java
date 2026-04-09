package it.unibo.samplejavafx.mvcexample.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class StandardCardTest {

    @Test
    void testCardValues() {
        // Test carta numerica standard
        final Card fiveOfHearts = new StandardCard(Rank.FIVE, Suit.HEARTS);
        assertEquals(5, fiveOfHearts.getValue());

        // Test Figura: deve valere 10, non importa se è J, Q o K
        final Card jackOfSpades = new StandardCard(Rank.JACK, Suit.SPADES);
        assertEquals(10, jackOfSpades.getValue());

        final Card kingOfDiamonds = new StandardCard(Rank.KING, Suit.DIAMONDS);
        assertEquals(10, kingOfDiamonds.getValue());

        // Test Asso: valore base 11
        final Card aceOfClubs = new StandardCard(Rank.ACE, Suit.CLUBS);
        assertEquals(11, aceOfClubs.getValue());
    }

    @Test
    void testCardNames() {
        final Card card = new StandardCard(Rank.QUEEN, Suit.SPADES);
        
        // Verifichiamo che il nome sia esattamente "QUEEN of SPADES"
        assertEquals("QUEEN of SPADES", card.getName());
        
        // Verifichiamo che anche il toString() faccia il suo dovere
        assertEquals("QUEEN of SPADES", card.toString());
    }
}