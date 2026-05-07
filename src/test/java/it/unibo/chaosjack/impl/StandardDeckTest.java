package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.CardModifier;
import it.unibo.chaosjack.model.api.Deck;
import it.unibo.chaosjack.model.impl.StandardDeck;

import java.util.Optional;

/**
 * Test class for StandardDeck.
 */
class StandardDeckTest {
    private static final int FULL_DECK = 52;
    private static final int DECK_MINUS_ONE = 51;
    private static final int CARDS_TO_DRAW = 2;
    private static final int DECK_AFTER_DRAW = 50;

    private static final int EXPECTED_EACH_SPECIAL = 4;
    private static final int EXPECTED_NORMAL_CARDS = 40;


    @Test
    void testInitialDeckSize() {
        final Deck deck = new StandardDeck();
        assertEquals(FULL_DECK, deck.remainingCards());
    }

    @Test
    void testDrawCard() {
        final Deck deck = new StandardDeck();
        final Optional<Card> drawnCard = deck.draw();
        assertTrue(drawnCard.isPresent());
        assertEquals(DECK_MINUS_ONE, deck.remainingCards());
    }

    @Test
    void testResetDeck() {
        final Deck deck = new StandardDeck();
        for (int i = 0; i < CARDS_TO_DRAW; i++) {
            deck.draw();
        }
        assertEquals(DECK_AFTER_DRAW, deck.remainingCards());

        deck.reset();
        assertEquals(FULL_DECK, deck.remainingCards());
    }

    @Test
    void testEmptyDeck() {
        final Deck deck = new StandardDeck();
        for (int i = 0; i < FULL_DECK; i++) {
            assertTrue(deck.draw().isPresent());
        }
        assertEquals(0, deck.remainingCards());
        final Optional<Card> impossibleCard = deck.draw();
        assertFalse(impossibleCard.isPresent());
    }

    @Test
    void testShuffleMaintainsIntegrity() {
        final Deck deck = new StandardDeck();
        deck.shuffle();
        deck.shuffle();
        assertEquals(FULL_DECK, deck.remainingCards());
    }

    @Test
    void testExactSpecialCardsDistribution() {
        final Deck deck = new StandardDeck();

        int bustCount = 0;
        int reverseCount = 0;
        int ghostCount = 0;
        int normalCount = 0;

        for (int i = 0; i < FULL_DECK; i++) {
            final Optional<Card> drawnCard = deck.draw();
            assertTrue(drawnCard.isPresent());

            final CardModifier mod = drawnCard.get().getModifier();

            if (mod == CardModifier.BUST_MAGNET) {
                bustCount++;
            } else if (mod == CardModifier.REVERSE) {
                reverseCount++;
            } else if (mod == CardModifier.GHOST) {
                ghostCount++;
            } else if (mod == CardModifier.NONE) {
                normalCount++;
            }
        }

        assertEquals(EXPECTED_EACH_SPECIAL, bustCount);
        assertEquals(EXPECTED_EACH_SPECIAL, reverseCount);
        assertEquals(EXPECTED_EACH_SPECIAL, ghostCount);
        assertEquals(EXPECTED_NORMAL_CARDS, normalCount);
    }
}
