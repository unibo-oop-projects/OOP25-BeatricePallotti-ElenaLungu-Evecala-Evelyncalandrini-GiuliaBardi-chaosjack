package it.unibo.chaosjack.model.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.Hand;
import it.unibo.chaosjack.model.api.Partecipant;

/**
 * Abstract class implementation of {@link Partecipant}.
 * It contains the shared logic for the name, management of cards an the calculation of the score
 */

public abstract class AbstractPlayer implements Partecipant {

    private final String name;
    private HandImpl hand;

    /**
     * Constructor for a new Abstract Player.
     * 
     * @param name of the partecipant
     */
    public AbstractPlayer(final String name) {
        this.name = name;
        this.hand = new HandImpl();

    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final void addCard(final Card card) {
        this.hand.addCard(card);
    }

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP", 
        justification = "Required for View/Controller state sync."
    )
    @Override
    public final Hand getHand() {
        return this.hand;
    }

    @Override
    public final void resetHand() {
        this.hand = new HandImpl();
    }
}
