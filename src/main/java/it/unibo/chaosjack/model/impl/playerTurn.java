package it.unibo.chaosjack.model.impl;
import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.gameEngine;
import it.unibo.chaosjack.model.api.turnState;

public class playerTurn  implements turnState{
    /* gestisco il turno del giocatore */
   
   private final gameEngine game;

   public playerTurn(gameEngine game){
    this.game = game;
   }

   @Override
    public void hit(){
        Card cartaPescata = game.getDeck().drawCard();
        game.getPlayerHand().addCard(cartaPescata);
        if ( game.getPlayerHand().getScore() > 21) {
            // qui il banco vince perchè il giocatore ha sballato
        }// il metodo aggiunge una carta alla mia mano
        //metoodo per controllare se ho sballato e per avere lo score della mia mano
    }
       

    @Override
    public void stand(){ // cambio lo il turno ( passo al banco )
        game.changeState(new dealerTurn(game));// passo il turno al giocatore successivo o al banco 

    }

    @Override
    public void doubleDown(){
    }

    @Override
    public String getStateName() {
        return "playerTurn";
    }

}
