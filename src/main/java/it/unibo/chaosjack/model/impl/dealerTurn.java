package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.gameEngine;
import it.unibo.chaosjack.model.api.turnState;
import it.unibo.chaosjack.model.api.Card;

public class dealerTurn implements turnState { 
  private final gameEngine game;
  public dealerTurn(gameEngine game){
    this.game = game;
  }

  
    
  @Override
  public void hit() {
    Card cartaPescata = game.getDeck().drawCard(); // il banco pesca una carta
    game.getDealerHand().addCard(cartaPescata); // aggiungo la carta
  }

  @Override
  public void stand() {
    // il banco si ferma e si confronta il punteggio con quello del giocatore per decidere chi vince
  }

  @Override
    public void doubleDown() {
    }

  @Override
    public String getStateName() {
        return "dealerTurn";
    }

/* gestisco il turno del banco  */    
}


