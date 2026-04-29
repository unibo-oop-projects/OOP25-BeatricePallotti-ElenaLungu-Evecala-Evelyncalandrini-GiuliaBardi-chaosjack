package it.unibo.chaosjack.model.impl;
import java.util.List;
import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.GameRule;

// DEVO VALUTARE SE FAR SCENDERE IL PUNTEGGIO SOTTO 0 O SE FERMARLO LI 
public class YingYungTurn implements GameRule {
    @Override
    public int calculateScore(List<Card> cards) {
        int score = 0;
        for ( Card card :cards) {
            if (card.getName().contains("HEARTS") || card.getName().contains("DIAMONDS")) {
                score -= card.getValue();
            }

            else {
                score += card.getValue();
            }
        }
        return score;
    }
} 
