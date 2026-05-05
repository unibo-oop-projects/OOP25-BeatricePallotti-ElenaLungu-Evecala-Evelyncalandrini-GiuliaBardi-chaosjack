package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.SpecialRound;
import java.util.List;
import it.unibo.chaosjack.model.api.Card;

public class YingYung implements SpecialRound{
    
    /**
     * this class represents a special round where the red cards are worth negative points, while the black cards are worth positive points
     */
    @Override
    public int specialScore(List<Card> playersCards){
        int score =0;
        for ( Card c: playersCards){
            if (c.getName().contains("HEARTS") || c.getName().contains("DIAMONDS")){
                score -= c.getValue();
            }
            else {
                score += c.getValue();
            }
            }
            /**
             * if the score is negative, it is set to 0 
             */
            if(score<0){
                score = 0;
            }
            return score;
        }
        
        
}
