package it.unibo.chaosjack.model.api;

import java.util.List;

/**
 * Models a generic hand for players and dealer.
 */

public interface Hand {

    /**
     * this method allows to add a card to the hand of player/dealer.
     * 
     * @param card the card to add to the hand.
     */
    void addCard(Card card);

    /**
     * get the score of a hand.
     * 
     * @return the score of the hand. 
     */
    int getScore();

    /**
     * says whether the cards are the same color or not.
     * 
     * @param cards the cards in the player's/dealer's hand.
     * @return true if all the cards have the same colour.
     */
    boolean sameColor(List<Card> cards);

    /**
     * get the cards to the hand.
     * 
     * @return the cards in the hand of the player/dealer.
     */
    List<Card> getCards();
}
