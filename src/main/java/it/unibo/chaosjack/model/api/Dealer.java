package it.unibo.chaosjack.model.api;

/**
 * This interface represents the Dealer in the Blackjack game.
 */
public interface Dealer extends Partecipant {

    /**
     * Decides whether the dealer should take another card.
     * The dealer typically hits until the hand reaches a total of 17.
     * 
     * @param currentScore score of the dealer
     * @return true if the dealer should hit
     */
    boolean shouldHit(int currentScore);
}
