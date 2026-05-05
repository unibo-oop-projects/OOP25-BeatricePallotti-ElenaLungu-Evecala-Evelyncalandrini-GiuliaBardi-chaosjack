package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.Partecipant;

/**
 * Abstract class implementation of {@link Partecipant}.
 * It contains the shared logic for the name, management of cards an the calculation
 * of the score
 */
public abstract class AbstractPlayer implements Partecipant {

    private final String name;
    private final Hand hand;

    /**
     * Constructor for a new Base Player.
     * 
     * @param name of the partecipant
     */
    public AbstractPlayer(final String name) {
        this.name = name;
        this.hand = new Hand();

    }

    /**
     * @return he name of the player.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Adds a card to the player's hand.
     */
    @Override
    public void addCard(final Card card) {
        this.hand.addCard(card);
    }

    /**
     * returns a view of the hand that can't be modified.
     */
    @Override
    public Hand getHand() {
        return this.hand;
    }

    /**
     * Clears the hand of the player.
     */
    @Override
    public void resetHand() {
        this.hand.getCards().clear();
    }

    
}

