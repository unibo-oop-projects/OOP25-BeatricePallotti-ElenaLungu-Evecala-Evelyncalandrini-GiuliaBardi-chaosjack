package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.SpecialRound;
import java.util.List;
import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.CardModifier;

/**
 * this class implements the interface SpecialRound. 
 * It represents a special round where the red cards are worth negative, 
 * points, while the black cards are worth positive points.
 */

public final class YingYung implements SpecialRound {

    @Override
    public int specialScore(final List<Card> playersCards) {
        int score = 0;
        for (final Card c: playersCards) {
            if (c.getModifier() == CardModifier.NONE) {
                if (c.getName().contains("HEARTS") || c.getName().contains("DIAMONDS")) {
                    score -= c.getValue();

                } else {
                    score += c.getValue();
                }
            } else {
                score += c.getValue();
            }
        }
        return score;
    }

    @Override
    public String getDescription() {
        return "Ying Yung";
    }
}
