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
     * Constructor for a new Abstract Player.
     * 
     * @param name of the partecipant
     */
    public AbstractPlayer(final String name) {
        this.name = name;
        this.hand = new Hand();

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void addCard(final Card card) {
        this.hand.addCard(card);
    }

    @Override
    public Hand getHand() { //devo controllare questo,fare in modo che 
        return this.hand;
    }

    @Override
    public void resetHand() {
        this.hand.getCards().clear();
    }

    
}

