package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.TurnState;

import java.util.Optional;

import it.unibo.chaosjack.model.api.Card;

public class DealerTurn implements TurnState { 
  private final GameEngine game;
  public DealerTurn(GameEngine game){
    this.game = game;
  }

  
    
  @Override
  public void hit() {
    Optional<Card> controlloCarta = game.getDeck().draw(); //questo metodo andrà bene quando elena farà il push
        
        if (controlloCarta.isPresent()){ // controllo che il valore della carta non sia nullo (che il mazzo non sia vuoto)
            Card cartaPescata = controlloCarta.get(); // se la carta è presente allora la assegno a una carta vera e propria e la aggiungo alla mano
            game.getDealerHand().addCard(cartaPescata);
        }
  }

  @Override
  public boolean stand() {
    
    return true;
  }

  @Override
  public boolean doubleDown() {
    return false; // i bot non possono raddoppiare 
  }

  

  

/* gestisco il turno del banco  */    
}
