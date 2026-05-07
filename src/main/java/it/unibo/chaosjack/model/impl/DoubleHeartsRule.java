package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.SpecialRound;
import it.unibo.chaosjack.model.api.Card;
import java.util.List;


public class DoubleHeartsRule implements SpecialRound {
    @Override
    public final int specialScore(final List<Card> playersCards) {
        int score = 0;
        for (final Card c : playersCards) {
            if (c.getName().contains("HEARTS")) {
                score += c.getValue() * 2;
            } else {
                score += c.getValue();
            }
        }
        return score;
    }

}
