package it.unibo.chaosjack.model.api;
import java.util.List;

public interface SpecialRound {

    /**
     * @return the score of special round 
     */
    int specialScore(List<Card> playersCards);
    
   
}
