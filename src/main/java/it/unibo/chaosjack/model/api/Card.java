package it.unibo.chaosjack.model.api;

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

    /**
     * Gets the special modifier of the card.
     * 
     * @return the card modifier, or NONE if it's a standard card
     */
    default CardModifier getModifier() {
        return CardModifier.NONE;
    }
}

