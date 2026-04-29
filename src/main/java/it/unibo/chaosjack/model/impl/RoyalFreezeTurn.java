package it.unibo.chaosjack.model.impl;
import it.unibo.chaosjack.model.api.GameRule;
import java.util.List;
import it.unibo.chaosjack.model.api.Card;

public class RoyalFreezeTurn implements GameRule {
    
    @Override
    public int calculateScore( List<Card> cards) {
        int score = 0;
        for ( Card card : cards) {
            if (card.getName().contains("KING") || card.getName().contains("QUEEN") || card.getName().contains("JACK")) {
                score += 0;
            } else {
                score += card.getValue();
            }
        }
        return score;
    }
}
