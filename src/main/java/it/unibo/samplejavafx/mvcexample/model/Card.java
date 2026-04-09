package it.unibo.samplejavafx.mvcexample.model;

/**
 * Interface representing a generic game card.
 */
public interface Card {
    /**
     * @return the Blackjack value of the card
     */
    int getValue();
    
    /**
     * @return the full name of the card (e.g., "ACE of HEARTS")
     */
    String getName();
}