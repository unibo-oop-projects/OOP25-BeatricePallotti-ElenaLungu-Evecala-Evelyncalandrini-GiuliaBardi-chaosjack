package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.Partecipant;
import it.unibo.chaosjack.model.api.Deck;

import java.util.List;
import java.util.Optional;
import it.unibo.chaosjack.model.api.SpecialRound;
import it.unibo.chaosjack.model.api.Dealer;

/**
 * This class implements the GameEngine interface and represents the core of the game logic.
 */
public class GameEngineImpl implements GameEngine {

    private Deck deck;
    private Dealer dealer;
    private List<Partecipant> players;
    //private Wallet walletPlayer;
    private int currentPlayerIndex = -1;
    private Optional<SpecialRound> specialRound = Optional.empty();
    private Partecipant currentPlayer;

    public GameEngineImpl( Deck deck, List<Partecipant> players, Dealer dealer) { // ricorda di aggiuungere il wallet
        this.deck = deck;
        this.players = players;
        this.dealer = dealer;

        this.nextTurn();
    }

    /**
     * @param specialRound
     * this method allows to set a special round, if the parameter is null the special round is removed 
     * and the game returns to normal
     */
    @Override
    public void setSpecialRound(SpecialRound  specialRound){
        this.specialRound = Optional.ofNullable(specialRound);
    }

    /**
     * @param hand
     * @return This method calculates the score following the rules of the current shift, 
     * if a special shift is active it will calculate the score based on the shift rules,
     *  otherwise it will calculate it normally
    */
   @Override
    public int currentScore(Hand hand){
        if(this.specialRound.isPresent()){
            return this.specialRound.get().specialScore(hand.getCards());
        } else {
            return hand.getScore();
        }
    }
    

    /**
     * this method allows to change the state of the game (switch from player's turn to dealer's turn and vice versa)
     */
    /*@Override
    public void changeState(TurnState newState){
        this.currentState = newState; // questo metodo mi permette di cambiare lo stato del gioco (passare dal turno del giocatore a quello del banco e viceversa)
        
    }*/

    /**
     * @return the deck of the game
     */
    @Override
    public Deck getDeck() {
        return deck;
    }

    
    @Override
    public Hand getDealerHand() {
        return this.dealer.getHand();
    }

    

    /**
     * @param name is the game player's name
     * @return the score of the player with the given name, if there is no player with that name it returns 0
     */
    @Override
    public int getPlayerScore(String name) { 
        for ( Partecipant p : players) {
           if ( p.getName().equals(name) ) {
             return p.getHand().getScore();
           }
        }
        return 0; 
    }


    /**
     * This method handles turn switching between players whether they are bot or not, 
     */
    @Override
    public void nextTurn() {
       currentPlayerIndex++; 
         if (currentPlayerIndex < players.size()) { 
            this.currentPlayer = players.get(currentPlayerIndex);
            
        } 
        /**
         if there aren't other players, the turn passes to the dealer
        */
         else {
            this.currentPlayer = dealer;
        }
    }

    /**
     * @return the list of players in the game
     */
    @Override
    public List<Partecipant> getPlayers() {
        return players;
    }

    @Override
    public void hit(){
        
    }

    @Override
    public void stand(){
        this.nextTurn();
    }

    @Override
    public Partecipant getCurrentPlayer() {
        return this.currentPlayer;
    }

    
   }


