package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.Dealer;



/**
 * Implementation of {@link Dealer} interface.
 */

public final class DealerImpl extends AbstractPlayer implements Dealer {

   private static final int STAY_THRESHOLD = 17;

   /**
    * Constructs a new Dealer with the default name "Dealer".
    */
   public DealerImpl() { //constructor
     super("Dealer");
  }

    @Override
    public boolean shouldHit(int currentScore) {
         return currentScore < STAY_THRESHOLD; 
    }
}

