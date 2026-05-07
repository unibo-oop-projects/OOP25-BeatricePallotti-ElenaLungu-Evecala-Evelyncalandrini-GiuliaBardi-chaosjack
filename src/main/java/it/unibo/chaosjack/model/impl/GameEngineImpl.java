package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.Partecipant;
import it.unibo.chaosjack.model.api.Deck;

import java.util.List;
import java.util.Optional;

import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.SpecialRound;
import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.Dealer;

/**
 * This class implements the GameEngine interface and represents the core of the game logic.
 */
public class GameEngineImpl implements GameEngine {

    private final Deck deck;
    private final Dealer dealer;
    private final List<Partecipant> players;
    private int currentPlayerIndex = 0;
    private Optional<SpecialRound> specialRound = Optional.empty();
    private Partecipant currentPlayer;
    private Table.State currentState = Table.State.FIRST_BET;
    private final Table table;

    public GameEngineImpl(final Deck deck, final List<Partecipant> players, final Dealer dealer, final Table table) { 
        this.deck = deck;
        this.players = players;
        this.dealer = dealer;
        this.table= table;
    }

    @Override
    public void setSpecialRound(final SpecialRound  specialRound) {
        this.specialRound = Optional.ofNullable(specialRound);
    }

   @Override
    public int currentScore(final HandImpl hand) {
        if (this.specialRound.isPresent()){
            return this.specialRound.get().specialScore(hand.getCards());
        } else 
        {
            return hand.getScore();
        }
    }
    
    @Override
    public Deck getDeck() {
        return deck;
    }

    @Override
    public HandImpl getDealerHand() {
        return this.dealer.getHand();
    }

    @Override
    public int getPlayerScore(final String name) { 
        for (final Partecipant p : players) {
           if (p.getName().equals(name)) {
             return p.getHand().getScore();
            }
        }
        return 0; 
    }

    @Override
    public void nextTurn() {
        if (this.currentState == Table.State.PLAYING ){
            currentPlayerIndex++; 
         if (currentPlayerIndex < players.size()) { 
            this.currentPlayer = players.get(currentPlayerIndex);
            
         } else {
            this.table.stepPassage();
         }
        } else {
            throw new IllegalStateException("impossible to play ");
        }
       
    }

    @Override
    public void dealerTurn() {
        if (this.currentState == Table.State.DEALER_TURN) {
            
            this.dealer.playTurn(deck);

        } else 
        {
            throw new IllegalStateException("impossible to play ");
        }
    }

    @Override
    public List<Partecipant> getPlayers() {
        return players;
    }

    
    @Override
    public void stand() {
        if(table.getCurrentState() == Table.State.DEALER_TURN) {
            this.table.stepPassage();
        } else {
            this.nextTurn();
        }
    }

    @Override
    public Partecipant getCurrentPlayer() {
        return this.currentPlayer;
    }
   }
