package it.unibo.chaosjack.model.impl;

/**
 * Enumeration for standard card ranks and their base Blackjack values.
 */
public enum Rank {
    ACE(11), TWO(2), THREE(3), FOUR(4), FIVE(5), 
    SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), 
    JACK(10), QUEEN(10), KING(10);

    private final int value;

    Rank(final int value) {
        this.value = value;
    }

    /**
     * Gets the value of the rank.
     * 
     * @return the integer value of the card's rank
     */
    public int getValue() {
        return this.value;
    }
}
