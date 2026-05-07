package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.lang.module.ModuleDescriptor.Exports.Modifier;
import it.unibo.chaosjack.model.impl.HandImpl;
import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.CardModifier;
import it.unibo.chaosjack.model.impl.StandardCard;
import org.junit.jupiter.api.Test;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.Suit;
import it.unibo.chaosjack.model.api.Hand;


public class HandTest {
    @Test

    public void addCard(){
        final HandImpl myHand = new HandImpl();
        final Card card1 = new StandardCard(Rank.TWO, Suit.HEARTS);

        myHand.addCard(card1);
        assertEquals(1, myHand.getCards().size());

        assertEquals(2, myHand.getCards().get(0).getValue());

        assertEquals("TWO of HEARTS", myHand.getCards().get(0).getName());

    }

    @Test
    public void getScore() {
        final Hand myHand = new HandImpl();
        final Card card1 = new StandardCard(Rank.TWO, Suit.HEARTS, CardModifier.NONE);
        final Card card2 = new StandardCard(Rank.JACK, Suit.SPADES, CardModifier.NONE);
        final Card card3 = new StandardCard(Rank.ACE, Suit.CLUBS, CardModifier.NONE);

        myHand.addCard(card1);
        myHand.addCard(card2);
        myHand.addCard(card3);

        assertEquals(13, myHand.getScore());
    }

    @Test
    public void sameColor() {
        final Hand myHand = new HandImpl();
        final Card card1 = new StandardCard(Rank.TWO, Suit.HEARTS, CardModifier.NONE);
        final Card card2 = new StandardCard(Rank.THREE, Suit.HEARTS, CardModifier.NONE);
        final Card card3 = new StandardCard(Rank.FOUR, Suit.CLUBS, CardModifier.NONE);

        myHand.addCard(card1);
        myHand.addCard(card2);
        myHand.addCard(card3);

        assertEquals(false, myHand.sameColor(myHand.getCards()));
    }

    @Test
    public void specialCardRound() {
        Hand playerHand = new HandImpl();
        Card card4 = new StandardCard(Rank.SIX, Suit.DIAMONDS, CardModifier.BUST_MAGNET);
        Card card5 = new StandardCard(Rank.EIGHT, Suit.CLUBS, CardModifier.NONE);

        playerHand.addCard(card4);

        assertEquals(12,playerHand.getScore());

        playerHand.addCard(card5);
        assertEquals(20, playerHand.getScore());

    }
}
