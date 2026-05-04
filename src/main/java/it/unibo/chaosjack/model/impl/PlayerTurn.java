package it.unibo.chaosjack.model.impl;
import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.TurnState;
import java.util.Optional;

public class PlayerTurn  implements TurnState{
    /* gestisco il turno del giocatore */
   
   private final GameEngine game;
   private int myIndex;

   public PlayerTurn(GameEngine game, int index){
    this.game = game;
    this.myIndex = index;
   }

   @Override
    public void hit(){
        Optional<Card> controlloCarta = game.getDeck().draw(); 
        /**
         * check that the deck is not empty 
         */
        if (controlloCarta.isPresent()){ 
            Card cartaPescata = controlloCarta.get(); 
            /**
             * if the deck is not empty,add the drawn card to the player's hand
             */
            game.getPlayers().get(myIndex).getHand().addCard(cartaPescata);
        }

        if (game.getPlayers().get(myIndex).getHand().getScore() > 21) { 
            /**
             * if the player's score is greater than 21, the player automatically passes his turn
             */
            stand();
        }
    }
       

    @Override
    public boolean stand(){ 
        game.nextTurn(); 
        return true;
    }

    @Override
    public boolean doubleDown() {
        /**
         * I check if the player has enough money to double bet
         */
        if ( game.getPlayers().get(myIndex).getBet() *2 < game.getPlayers().get(myIndex).getWallet().getBalance() ) { // controllo che il giocatore abbia abbastanza soldi per raddoppiare
            this.hit(); 
            this.stand(); 
            return true;
        } else{
            /**
             * If the player doesn't have enough money to double the bet the method does nothing
             */
            return false; 
        }
    }

    
}
