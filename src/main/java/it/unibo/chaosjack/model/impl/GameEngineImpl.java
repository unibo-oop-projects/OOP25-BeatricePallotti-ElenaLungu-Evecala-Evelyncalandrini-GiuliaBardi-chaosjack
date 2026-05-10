package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.Hand;
import it.unibo.chaosjack.model.api.Partecipant;
import it.unibo.chaosjack.model.api.Deck;

import java.util.List;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.SpecialRound;

import it.unibo.chaosjack.model.api.Dealer;

/**
 * This class implements the GameEngine interface and represents the core of the game logic.
 */
public final class GameEngineImpl implements GameEngine {

    private final Deck deck;
    private final Dealer dealer;
    private final List<Partecipant> players;
    private int currentPlayerIndex = 0;
    private Optional<SpecialRound> specialRound = Optional.empty();
    private Partecipant currentPlayer;
    private Table table;

    /**
     * constructor for the GameEngineImpl class.
     * 
     * @param deck is the deck used during the game.
     * @param players is the list of player  playing. 
     * @param dealer is the dealer of the game.
     */
    public GameEngineImpl(final Deck deck, final List<Partecipant> players, final Dealer dealer) { 
        this.deck = deck;
        this.players = List.copyOf(players);
        this.dealer = dealer;
    }

    @Override
    public void setTable(final Table table) {
        if (table != null) {
            this.table = table;
        } else {
            throw new IllegalArgumentException("the table cannot be null.");
        }
    }

    @Override
    public void setSpecialRound(final SpecialRound specialRound) {
        this.specialRound = Optional.ofNullable(specialRound);
    }

   @Override
    public int currentScore(final HandImpl hand) {
        if (this.specialRound.isPresent()) {
            return this.specialRound.get().specialScore(hand.getCards());
        } else {
            return hand.getScore();
        }
    }

    @Override
    public Deck getDeck() {
        return deck;
    }

    @Override
    public Hand getDealerHand() {
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
        if (table.getCurrentState() == Table.State.PLAYING) {
            
         if (currentPlayerIndex < players.size()) { 
            this.currentPlayer = players.get(currentPlayerIndex);
            ++currentPlayerIndex;
         } else {
            this.table.stepPassage();
         }
        } else {
            throw new IllegalStateException("impossible to play ");
        }

    }

    @Override
    public void dealerTurn() {
        if (this.table.getCurrentState() == Table.State.DEALER_TURN) { 
            if (dealer != null) {
                this.currentPlayer = dealer;
                this.dealer.playTurn(deck);
            } else {
                throw new IllegalArgumentException("the dealer cannot be null.");
            }
        } else {
            throw new IllegalStateException("impossible to play ");
        }
    }

    @Override
    public List<Partecipant> getPlayers() {
        return List.copyOf(players);
    }

    @Override
    public void stand() {
        if (table.getCurrentState() == Table.State.DEALER_TURN) {
            this.table.stepPassage();
        } else {
            this.nextTurn();
        }
    }

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "Returning the original Player reference is important to allow the view to stay up to date with the game state."
    )
    @Override
    public Partecipant getCurrentPlayer() {
        return this.currentPlayer;
    }
   }
