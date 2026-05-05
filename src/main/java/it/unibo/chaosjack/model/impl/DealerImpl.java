package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.Dealer;
import it.unibo.chaosjack.model.api.Deck;

/**
 * Implementation of {@link Dealer} interface.
 */
public class DealerImpl  extends AbstractPlayer implements Dealer {

   private final int  STAY_THRESHOLD = 17;

  /**
   * Constructs a new Dealer with the default name "Dealer".
   */
   public  DealerImpl() { //constructor
     super("Dealer");
  }

  /**
   * @return if the dealer should hit
   */
    @Override
    public boolean shouldHit() {
         return this.getHand().getScore() < STAY_THRESHOLD; 
    }

    /**
     * Execute the dealer's urn automatically.
     * @param deck
     */
    @Override
    public void playTurn(final Deck deck) {
       while (this.shouldHit()){
         deck.draw().ifPresent(this :: addCard);
       }
    }

    
    
}

