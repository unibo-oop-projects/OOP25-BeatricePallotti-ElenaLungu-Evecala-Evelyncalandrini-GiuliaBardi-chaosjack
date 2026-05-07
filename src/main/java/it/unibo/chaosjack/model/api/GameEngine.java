package it.unibo.chaosjack.model.api;

import it.unibo.chaosjack.model.impl.HandImpl;
import java.util.List;

public interface GameEngine {

    /**
     * 
     * @return the current player of the game
     */
    Partecipant getCurrentPlayer();

    /**
     * this method is use to change the turn of the game
     */
    void nextTurn();

    /**
     * 
     * @return the Deck of the table
     */
    Deck getDeck(); 

    /**
     * 
     * @return the hand of the dealer
     */
    HandImpl getDealerHand(); 

    /**
     * 
     * @return the score of a player by their name
     */
    int getPlayerScore(String name); 

    /**
     * 
     * @return the list of players in the game
     */
    List<Partecipant> getPlayers();

    /**
     * allows the player to pass the turn after
     */
    void stand(); 

    /** 
     * 
     * @param hand is the hand of the player o dealer
     * @return the score of the hand with the rules of the spcial round or the normal rules if there isn't a special round
     */    
     int currentScore(HandImpl hand); 
    
     /**
      * @param specialRound contains the special round you want to active
      * this method allows to set a special round or remove if the parameter is null
      */
     void setSpecialRound(SpecialRound specialRound); 

     /**
      * this method allows the dealer to play
     */
    void dealerTurn();

}
