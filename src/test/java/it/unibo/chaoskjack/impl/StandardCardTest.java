package it.unibo.chaoskjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.CardModifier;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.StandardCard;
import it.unibo.chaosjack.model.impl.Suit;

/**
 * Test class for StandardCard, including Chaos modifiers edge cases.
 */
class StandardCardTest {

    private static final int LOW_VAL = 5;
    private static final int FACE_VAL = 10;
    private static final int ACE_VAL = 11;

    // Costanti per il Caos (niente numeri magici per PMD)
    private static final int BUST_VAL = 12;
    private static final int GHOST_VAL = 0;
    private static final int REVERSE_LOW_VAL = -5;
    private static final int REVERSE_ACE_VAL = -11;

    @Test
    void testStandardCardValues() {
        final Card fiveOfHearts = new StandardCard(Rank.FIVE, Suit.HEARTS);
        assertEquals(LOW_VAL, fiveOfHearts.getValue());

        final Card jackOfSpades = new StandardCard(Rank.JACK, Suit.SPADES);
        assertEquals(FACE_VAL, jackOfSpades.getValue());

        final Card kingOfDiamonds = new StandardCard(Rank.KING, Suit.DIAMONDS);
        assertEquals(FACE_VAL, kingOfDiamonds.getValue());

        final Card aceOfClubs = new StandardCard(Rank.ACE, Suit.CLUBS);
        assertEquals(ACE_VAL, aceOfClubs.getValue());
    }

    @Test
    void testBustMagnetModifier() {
        final Card bustTwo = new StandardCard(Rank.TWO, Suit.HEARTS, CardModifier.BUST_MAGNET);
        assertEquals(BUST_VAL, bustTwo.getValue());
        assertEquals(CardModifier.BUST_MAGNET, bustTwo.getModifier());

        final Card bustAce = new StandardCard(Rank.ACE, Suit.SPADES, CardModifier.BUST_MAGNET);
        assertEquals(BUST_VAL, bustAce.getValue());
    }

    @Test
    void testReverseModifier() {
        final Card reverseFive = new StandardCard(Rank.FIVE, Suit.CLUBS, CardModifier.REVERSE);
        assertEquals(REVERSE_LOW_VAL, reverseFive.getValue());
        assertEquals(CardModifier.REVERSE, reverseFive.getModifier());

        final Card reverseAce = new StandardCard(Rank.ACE, Suit.DIAMONDS, CardModifier.REVERSE);
        assertEquals(REVERSE_ACE_VAL, reverseAce.getValue());
    }

    @Test
    void testGhostModifier() {
        final Card ghostQueen = new StandardCard(Rank.QUEEN, Suit.HEARTS, CardModifier.GHOST);
        assertEquals(GHOST_VAL, ghostQueen.getValue());
        assertEquals(CardModifier.GHOST, ghostQueen.getModifier());
    }

    @Test
    void testCardNamesWithModifiers() {
        final Card normalCard = new StandardCard(Rank.QUEEN, Suit.SPADES);
        assertEquals("QUEEN of SPADES", normalCard.getName());
        assertEquals("QUEEN of SPADES", normalCard.toString());

        // Nome con modificatore
        final Card chaosCard = new StandardCard(Rank.TEN, Suit.DIAMONDS, CardModifier.REVERSE);
        assertEquals("[REVERSE] TEN of DIAMONDS", chaosCard.getName());
        assertEquals("[REVERSE] TEN of DIAMONDS", chaosCard.toString());
    }
}
