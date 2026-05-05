package it.unibo.chaosjack.model.impl;
import it.unibo.chaosjack.model.api.SpecialRound;
import it.unibo.chaosjack.model.api.Card;
import java.util.List;

public class DoubleHeartsRule implements SpecialRound{
    
/**
 * This class represents a special round where the heart cards are worth double
 */
    @Override
    public int specialScore(List<Card> playersCars){
        int score = 0;
        for (Card c : playersCars) {
            if (c.getName().contains("HEARTS")) {
                score += c.getValue()*2;
            }
            else {
                score += c.getValue();
            }
        }
        return score;
    }

    
}
