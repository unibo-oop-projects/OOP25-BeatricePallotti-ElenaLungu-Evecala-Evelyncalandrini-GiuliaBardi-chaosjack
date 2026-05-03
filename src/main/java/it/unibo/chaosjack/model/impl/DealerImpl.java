package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.*;
/**
 * Implementation of {@link Dealer} interface
 */
public class DealerImpl  extends BasePlayer implements Dealer{

  /**
   * Constructs a new Dealer with the default name "Dealer"
   */
  public  DealerImpl(){ //constructor
     super("Dealer");
  }

  /**
   * @return if the dealer should hit
   */
    @Override
    public boolean shouldHit() {
         return this.getScore() < 17; //says if it is convinient to draw
    }

    /**
     * Execute the dealer's urn automatically
     */
    @Override
    public void playTurn(Deck deck) {
       while ( this.shouldHit() ){
         deck.draw().ifPresent(this :: addCard);
       }
       
    }
    
}
