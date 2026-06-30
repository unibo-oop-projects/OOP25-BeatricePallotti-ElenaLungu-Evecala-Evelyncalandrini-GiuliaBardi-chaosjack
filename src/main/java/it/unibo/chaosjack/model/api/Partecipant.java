package it.unibo.chaosjack.model.api;

/**
 * This interface represents a generic player in Blackjack.
 */

public interface Partecipant {

    int MAX_SCORE = 21;

    /**
     * returns the name of the partecipant.
     * 
     * @return the partecipant's name
     */
    String getName();

    /**
     * Clears the hand of the partecipants to start a new round.
     */
    void resetHand();

    /**
     * This methods adds a card in the hand of the partecipant.
     * 
     * @param card to add
     */
    void addCard(Card card);

    /**
     * Checks if the player's score exceeds the maximum limit of 21.
     * 
     * @param currentScore score of the partecipant
     * @return true if the partecipant is busted
     */
    default boolean isBusted(final int currentScore) {
        return currentScore > MAX_SCORE;
    }

    /**
     * Provides a list of all cards currently held by the player.
     * 
     * @return the list of the cards in the partecipant's hands
     */
    Hand getHand();

}
