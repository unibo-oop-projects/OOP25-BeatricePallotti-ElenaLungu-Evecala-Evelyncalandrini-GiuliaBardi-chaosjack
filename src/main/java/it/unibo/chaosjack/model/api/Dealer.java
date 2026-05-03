package it.unibo.chaosjack.model.api;

/**
 * This interface represents the Dealer in the Blackjack game.
 */
public interface Dealer extends Partecipant {

    /**
     * Decides whether the dealer should take another card.
     * The dealer typically hits until the hand reaches a total of 17.
     * @return true if the dealer should hit
     */
    boolean shouldHit();

    /**
     * Manages the dealer's entire turn.
     * This method automates the process of drawing cards from the deck
     * based on the dealer's hit conditions.
     */
    void playTurn(Deck deck);
}