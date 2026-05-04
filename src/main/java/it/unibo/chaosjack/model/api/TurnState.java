package it.unibo.chaosjack.model.api;

public interface TurnState {
    /**
     * this interface represents the state of the game, 
     * This interface represents the states of the game, it is implemented based on the actions that game participants can and cannot do
     */

    
    /**
     * Allows you to draw a card
     */
    void hit();

    /**
     * allows you to advance to the next round 
     */
    boolean stand();

    /**
     * allows you to pass the turn to the next player or the dealer
     */
    boolean doubleDown();
    
}
