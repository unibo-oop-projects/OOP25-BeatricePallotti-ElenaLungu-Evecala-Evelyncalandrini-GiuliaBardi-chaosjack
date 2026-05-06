package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.CardModifier;

/**
 * Implementation of a standard playing card with optional modifiers.
 */
public final class StandardCard implements Card {

    private static final int BUST_VALUE = 12;
    private static final int GHOST_VALUE = 0;

    private final Rank rank;
    private final Suit suit;
    private final CardModifier modifier;

    /**
     * Standard constructor for a card without modifiers.
     * This ensures compatibility with existing code.
     *
     * @param rank the rank of the card
     * @param suit the suit of the card
     */
    public StandardCard(final Rank rank, final Suit suit) {
        this(rank, suit, CardModifier.NONE);
    }

    /**
     * Constructs a card with a specific modifier for Chaos Jack mechanics.
     *
     * @param rank the rank of the card
     * @param suit the suit of the card
     * @param modifier the special modifier of the card
     */
    public StandardCard(final Rank rank, final Suit suit, final CardModifier modifier) {
        this.rank = rank;
        this.suit = suit;
        this.modifier = modifier;
    }

    @Override
    public int getValue() {
        switch (this.modifier) {
            case BUST_MAGNET:
                return BUST_VALUE;
            case REVERSE:
                return -this.rank.getValue();
            case GHOST:
                return GHOST_VALUE;
            case NONE:
                return this.rank.getValue();
        }
        return this.rank.getValue(); 
    }

    @Override
    public CardModifier getModifier() {
        return this.modifier;
    }

    @Override
    public String getName() {
        final String baseName = this.rank + " of " + this.suit;
        if (this.modifier == CardModifier.NONE) {
            return baseName;
        }
        return "[" + this.modifier + "] " + baseName;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
