package it.unibo.samplejavafx.mvcexample.model;

import java.util.Optional;

/**
 * Interface representing a deck of cards.
 */
public interface Deck {
    
    /**
     * Draws a card from the top of the deck.
     * Uses Optional to safely handle the case where the deck is empty.
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
}