package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.SpecialRound;
import java.util.List;
import it.unibo.chaosjack.model.api.Card;

/**
* this class implements the interface SpecialRound. It represents a special round where the red cards are worth negative 
* points, while the black cards are worth positive points.
*/

public class YingYung implements SpecialRound{
    
    @Override
    public int specialScore( final List<Card> playersCards){
        int score = 0;
        for (final Card c: playersCards) {
            if (c.getName().contains("HEARTS") || c.getName().contains("DIAMONDS")) {
                score -= c.getValue();
            }
            else 
            {
                score += c.getValue();
            }
            }
            
            if (score < 0)
            {
                score = 0;
            }
            return score;
        }
}
