package it.unibo.chaosjack.model.api;

import java.util.List;

/**
 * Models the main engine of Blackjack game.
 */

public interface GameEngine {

    /**
     * get the player who is playing right now.
     * 
     * @return the current player of the game.
     */
    Partecipant getCurrentPlayer();

    /**
     * this method is use to change the turn of the game.
     */
    void nextTurn();

    /** 
     * get the deck of the game.
     * 
     * @return the Deck of the table.
     */
    Deck getDeck(); 

    /**
     * get the dealer's hand.
     * 
     * @return the hand of the dealer.
     */
    Hand getDealerHand(); 

    /** 
     * get the score of a specific player.
     * 
     * @param name is ne the name of the player whose score we want to know
     * @return the score of a player by their name.
     */
    int getPlayerScore(String name); 

    /** 
     * get the list of players in the game.
     * 
     * @return the list of players in the game.
     */
    List<Partecipant> getPlayers();

    /**
     * allows the player to pass the turn after.
     */
    void stand(); 

    /** 
     * calculates a player's hand score by following the active special round rules if present.
     * 
     * @param hand is the hand of the player o dealer.
     * @return the score of the hand 
     */
    int currentScore(Hand hand); 

    /**
     * set a special round, if it's present.
     * 
     * @param specialRound contains the special round you want to active.
     */
    void setSpecialRound(SpecialRound specialRound); 

    /**
     * this method allows the dealer to play.
     */
    void dealerTurn();

    /**
     * set the table for this game engine to allow a tunr synchronization.
     * 
     * @param table the teble implementation to link to this engine.
     */
    void setTable(Table table);

    /**
     * allow the players to draw a card from the deck.
     */
    void hit();

    /**
     * @return true if the game is over, false otherwise.
     */
    boolean isGameOver();

    /**
     * reset all game parameters.
     */
    void resetGame();

    /**
     * deals the initial cards to the players.
     */
    void initialCards();

    /**
     * get the active special round.
     * 
     * @return the active special round (null if there isn't active special round).
     */
    SpecialRound getSpecialRound();

    /**
     * deals the initial cards to the dealer.
     */
    void initialCardsDealer();

}
