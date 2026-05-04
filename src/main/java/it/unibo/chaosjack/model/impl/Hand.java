package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.Card;
import java.util.List;
import java.util.ArrayList;

public class Hand {

    /**
     * This class represents the player's or the dealer's hand
     */

    /**
     * the cards in the hand
     */
    private final List<Card> cards = new ArrayList<>();  

    
    /**
     * adds a card to the hand
     */
    public void addCard( Card card) {  
        cards.add(card);
    }

    /**
     * @return returns the card score in a standard round
     */
    public int getScore(){ 
        int score = 0;
        int assesCount = 0;

        for (int i = 0; i < cards.size(); i++) {
             score += cards.get(i).getValue();
              if ( cards.get(i).getValue() == 11) {
                assesCount++;
              }
        }

        /**
         * if the score is greater than 21 and there are aces in the hand, the score is reduced by 10 
         * for each ace until the score is less than or equal to 21 or there are no more aces to reduce
         */
        while ( score > 21 && assesCount > 0) { 
            score -=10;
            assesCount--;
        }

        return score;
    }


    /**
     * @param cards is the list of cards in the player's hand
     * @return true if all the cards in the hand are of the same color, false otherwise
     */
    public boolean sameColor(List<Card> cards){
       boolean firstIsRed = isRed(cards.get(0));
        for ( Card c : cards) {
            if(isRed(c) != firstIsRed) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param card
     * @return true if the card is red, false otherwise
     */
    private boolean isRed(Card card){
        return card.getName().contains("HEARTS") || card.getName().contains("DIAMONDS");
    }

    /**
    * @return the list of cards in the hand
    */
    public List<Card> getCards() {
        return cards;
    }

    

    
}
