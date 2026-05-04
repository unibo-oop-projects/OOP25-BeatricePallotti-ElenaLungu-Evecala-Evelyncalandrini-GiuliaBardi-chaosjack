package it.unibo.chaosjack.model.api;
import it.unibo.chaosjack.model.impl.Hand;
import java.util.List;
import it.unibo.chaosjack.model.impl.Player;

public interface GameEngine {
    

    void changeState(TurnState newState);
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
    List<Player> getPlayers();

    void hit(); // metodo per il controller
    void stand(); // metodo che richiama il controller
    
}
