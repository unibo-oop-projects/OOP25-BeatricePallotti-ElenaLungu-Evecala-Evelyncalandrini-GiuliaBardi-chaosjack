package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.Hand;


import java.util.List;
import java.util.ArrayList;

/**
* This class represents the player's or the dealer's hand
*/
public class HandImpl implements Hand{

    private static final int MAX_SCORE = 21;
    private static final int ACE_VALUE = 11;

    private final List<Card> cards = new ArrayList<>();  

    @Override
    public void addCard(final Card card) {
        cards.add(card);
    }

    @Override
    public int getScore() { 
        int score = 0;
        int assesCount = 0;

        for (int i = 0; i < cards.size(); i++) {
             score += cards.get(i).getValue();
              if (cards.get(i).getValue() == ACE_VALUE) 
            {
                assesCount++;
            }
        }

        while (score > MAX_SCORE && assesCount > 0) { 
            score -= 10;
            assesCount--;
        }

        return score;
    }

    @Override
    public boolean sameColor(final List<Card> cards) {
       boolean firstIsRed = isRed(cards.get(0));
        for (final Card c : cards) {
            if (isRed(c) != firstIsRed) {
                return false;
            }
        }
        return true;
    }

    private boolean isRed(Card card) {
        return card.getName().contains("HEARTS") || card.getName().contains("DIAMONDS");
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }    
}
