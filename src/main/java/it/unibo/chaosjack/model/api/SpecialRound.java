package it.unibo.chaosjack.model.api;

import java.util.List;

@FunctionalInterface
public interface SpecialRound {

    /**
     * 
     * @param the cards in the player's hand
     * @return the score of special round 
     */
    int specialScore(List<Card> playersCards);
    
}
