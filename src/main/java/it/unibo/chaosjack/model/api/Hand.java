package it.unibo.chaosjack.model.api;

import java.util.List;

public interface Hand {
    
    /**
     * 
     * this method allows to add a card to the hand of player/dealer
     */
    void addCard(final Card card);

    /**
     * 
     * @return the score of the hand 
     */
    int getScore();

    /**
     * 
     * @param cards the cards in the player's/dealer's hand
     * @return true if all the cards have the same colour
     */
    boolean sameColor(final List<Card> cards);

    /**
     * 
     * @return the cards in the hand of the player/dealer
     */
    List<Card> getCards();
}
