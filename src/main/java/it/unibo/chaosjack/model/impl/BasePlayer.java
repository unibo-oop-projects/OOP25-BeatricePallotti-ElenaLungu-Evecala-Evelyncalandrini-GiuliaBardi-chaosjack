package it.unibo.chaosjack.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import it.unibo.chaosjack.model.api.*;

/**
 * Abstract class implementation of {@link Partecipant}
 * It contains the shared logic for the name, management of cards an the calculation
 * of the score
 */
public abstract class BasePlayer implements Partecipant{

    private final String name;
    private final List<Card> hand;

    /**
     * Constructor for a new Base Player 
     * @param name of the partecipant
     */
    public  BasePlayer(final String name){
        this.name = name;
        this.hand = new ArrayList<>();

    }

    /**
     * @return he name of the player
     */
    @Override
    public String getName(){
        return this.name;
    }

    /**
     * Adds a card to the player's hand
     */
    @Override
    public void addCard(final Card card){
        this.hand.add(card);
    }

    /**
     * returns a view of the hand that can't be modified
     */
    @Override
    public List<Card> getHand(){
        return Collections.unmodifiableList(this.hand);
    }

    /**
     * Clears the hand of the player
     */
    @Override
    public void resetHand(){
        this.hand.clear();
    }

    /**
     * Calculates the score of the current hand
     * It implements the logic to handle the aces's value
     * @return the calculated score of the hand
     */
    @Override
    public int getScore() {
        
        int score = hand.stream()
                        .mapToInt(Card::getValue)
                        .sum();                   
        
        long acesCount = hand.stream()
                            .filter(card -> card.getValue() == Rank.ACE.getValue())
                            .count();

        while ( score > 21 && acesCount > 0){
            score -= 10;
            acesCount--;
        }
        return score;
    }
}
