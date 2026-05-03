package it.unibo.chaosjack.model.api;

import java.util.List;

/**
 * This interface represents a generic player in Blackjack
 */
public interface  Partecipant {

    /**
     * returns the name of the partecipant
     * @return the partecipant's name
     */
    String getName();

    /**
     * Clears the hand of the partecipants to start a new round
     */
     void resetHand();
    /**
     * Adds a card to the player's current hand.
     */
    void addCard(Card card);

    /**
     * @return  the total score of the cards currently in the player's hand.
     */
    int getScore();

    /**
     * Checks if the player's score exceeds the maximum limit of 21.
     */
    default boolean isBusted(){
        return getScore()> 21 ;
    }

    /**
     * Provides a list of all cards currently held by the player.
     */
    List<Card> getHand();

}
