package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.SpecialRound;
import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.CardModifier;

import java.util.List;

/**
 * this class implements the interface SpecialRound. In this round all the HEARTS cards are worth double.
 */

public final class DoubleHeartsRule implements SpecialRound {
    @Override
    public int specialScore(final List<Card> playersCards) {
        int score = 0;

        for (final Card c : playersCards) {
            if (c.getName().contains("HEARTS") && c.getModifier() == CardModifier.NONE) {
                score += c.getValue() * 2;
            } else {
                score += c.getValue();
            }

        }

        return score;
    }

    @Override
    public String getDescription() {
        return "Double Hearts Rule";
    }

}
