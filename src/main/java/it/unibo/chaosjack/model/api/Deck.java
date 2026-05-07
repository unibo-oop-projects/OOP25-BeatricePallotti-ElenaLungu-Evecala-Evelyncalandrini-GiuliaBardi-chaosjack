package it.unibo.chaosjack.model.api;

import java.util.Optional;

/**
 * Interface representing a deck of cards.
 */
public interface Deck {

    /**
     * Draws a card from the top of the deck.
     *
     * @return an Optional containing the drawn Card, or an empty Optional if the deck is empty.
     */
    Optional<Card> draw();

    /**
     * Shuffles the remaining cards in the deck.
     */
    void shuffle();

    /**
     * @return the number of cards left in the deck.
     */
    int remainingCards();

    /**
     * Resets the deck, clearing remaining cards and generating a new full set of 52 cards.
     */
    void reset();
}

