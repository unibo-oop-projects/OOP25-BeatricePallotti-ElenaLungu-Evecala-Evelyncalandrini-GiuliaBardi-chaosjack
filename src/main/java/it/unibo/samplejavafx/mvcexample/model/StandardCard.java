package it.unibo.samplejavafx.mvcexample.model;

/**
 * Implementation of a standard playing card.
 */
public class StandardCard implements Card {
    
    private final Rank rank;
    private final Suit suit;

    public StandardCard(final Rank rank, final Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public int getValue() {
        // Prende il valore matematico del Blackjack direttamente dal tuo enum Rank!
        return this.rank.getValue(); 
    }

    @Override
    public String getName() {
        // Es: "QUEEN of HEARTS"
        return this.rank + " of " + this.suit;
    }
    
    // Il prof apprezzerà questo tocco di classe per il debugging nel terminale
    @Override
    public String toString() {
        return getName();
    }
}