package it.unibo.chaosjack.model.api;

/**
 * Enumeration of the possible special modifiers for a card.
 */
public enum CardModifier {
    /** No special effect. */
    NONE,
    /** Forces the card value to 12. */
    BUST_MAGNET,
    /** Reverses the value of the card, making it negative. */
    REVERSE,
    /** Forces the card value to 0. */
    GHOST
}
