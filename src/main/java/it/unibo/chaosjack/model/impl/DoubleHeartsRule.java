package it.unibo.chaosjack.model.impl;
import java.util.List;
import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.GameRule;

public class DoubleHeartsRule implements GameRule {

    @Override
    public  int calculateScore( List<Card> cards){
      int score = 0;
      for ( Card card : cards) {
        if (card.getName().contains("HEARTS")) {
            score += card.getValue()*2;
        }
      }
      return score;
    }
    
}
