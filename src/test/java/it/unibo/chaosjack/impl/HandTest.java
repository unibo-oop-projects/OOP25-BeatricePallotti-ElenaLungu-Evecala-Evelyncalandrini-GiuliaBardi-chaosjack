package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import it.unibo.chaosjack.model.impl.HandImpl;
import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.CardModifier;
import it.unibo.chaosjack.model.impl.StandardCard;
import org.junit.jupiter.api.Test;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.Suit;
import it.unibo.chaosjack.model.api.Hand;

/**
 * Test for class Hand.
 */
class HandTest {
    private static final int SCORE_13 = 13;
    private static final int SCORE_12 = 12;
    private static final int SCORE_20 = 20;

    @Test
    void testAddCard() {
        final HandImpl myHand = new HandImpl();
        final Card card1 = new StandardCard(Rank.TWO, Suit.HEARTS);
        myHand.addCard(card1);
        assertEquals(1, myHand.getCards().size());

        assertEquals(2, myHand.getCards().get(0).getValue());

        assertEquals("TWO of HEARTS", myHand.getCards().get(0).getName());

    }

    @Test
    void testGetScore() {
        final Hand myHand = new HandImpl();
        final Card card1 = new StandardCard(Rank.TWO, Suit.HEARTS, CardModifier.NONE);
        final Card card2 = new StandardCard(Rank.JACK, Suit.SPADES, CardModifier.NONE);
        final Card card3 = new StandardCard(Rank.ACE, Suit.CLUBS, CardModifier.NONE);
        myHand.addCard(card1);
        myHand.addCard(card2);
        myHand.addCard(card3);
        assertEquals(SCORE_13, myHand.getScore());
    }

    @Test
    void testSameColor() {
        final Hand myHand = new HandImpl();
        final Card card1 = new StandardCard(Rank.TWO, Suit.HEARTS, CardModifier.NONE);
        final Card card2 = new StandardCard(Rank.THREE, Suit.HEARTS, CardModifier.NONE);
        final Card card3 = new StandardCard(Rank.FOUR, Suit.CLUBS, CardModifier.NONE);
        myHand.addCard(card1);
        myHand.addCard(card2);
        myHand.addCard(card3);
        assertFalse(myHand.sameColor(myHand.getCards()));
    }

    @Test
    void testSpecialCard() {
        final Hand playerHand = new HandImpl();
        final Card card4 = new StandardCard(Rank.SIX, Suit.DIAMONDS, CardModifier.BUST_MAGNET);
        final Card card5 = new StandardCard(Rank.EIGHT, Suit.CLUBS, CardModifier.NONE);

        playerHand.addCard(card4);

        assertEquals(SCORE_12, playerHand.getScore());

        playerHand.addCard(card5);
        assertEquals(SCORE_20, playerHand.getScore());

    }

}
