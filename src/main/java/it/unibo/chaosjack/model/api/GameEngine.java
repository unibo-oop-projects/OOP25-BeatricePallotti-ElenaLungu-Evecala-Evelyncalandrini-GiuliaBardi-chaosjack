package it.unibo.chaosjack.model.api;
import it.unibo.chaosjack.model.impl.Hand;
import java.util.List;


public interface GameEngine {
    

    //void changeState(TurnState newState);

      Partecipant getCurrentPlayer();
        

    void nextTurn();

    /**
     * @return the Deck of the table
     */
    Deck getDeck(); 

    /**
     * @return the hand of the dealer
     */
    Hand getDealerHand(); 


    /**
     * @return the score of a player by their name
     */
    int getPlayerScore(String name); 

    /**
     * @return the list of players in the game
     */
    List<Partecipant> getPlayers();

    void hit(); // metodo per il controller
    void stand(); // metodo che richiama il controller

    default int currentScore(Hand hand) {
        return 0;
    }
    
    default void setSpecialRound(SpecialRound  specialRound) {
        // Default implementation - can be overridden by subclasses
    }

}
