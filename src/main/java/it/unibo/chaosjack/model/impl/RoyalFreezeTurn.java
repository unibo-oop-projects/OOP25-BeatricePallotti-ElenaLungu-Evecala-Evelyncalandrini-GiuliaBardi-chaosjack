package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.SpecialRound;
import java.util.List;
import it.unibo.chaosjack.model.api.Card;

/**
     * This class implemets the interface SpecialRound. Rapresent a special round where KIING, QUEEN and JACK
     * have a value of 0
     */
public class RoyalFreezeTurn implements SpecialRound{

    
    @Override
    public int specialScore(final List<Card> playersCards) {
        int score =0;
        for ( final Card c :playersCards) {
            if (c.getName().contains("KING") || c.getName().contains("QUEEN") || c.getName().contains("JACK"))
            {
                score += 0;
            } 
            else 
            {
                score += c.getValue();
            }
         }
         return score;
        } 
    }
    