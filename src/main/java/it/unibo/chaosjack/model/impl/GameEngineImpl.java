package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.Hand;
import it.unibo.chaosjack.model.api.Partecipant;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.api.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.Table.State;
import it.unibo.chaosjack.model.api.SpecialRound;
import it.unibo.chaosjack.model.api.Dealer;

/**
 * This class implements the GameEngine interface and represents the core of the game logic.
 */
public final class GameEngineImpl implements GameEngine {

    private final Deck deck;
    private final Dealer dealer;
    private final List<Partecipant> players;
    private int currentPlayerIndex;
    private Optional<SpecialRound> specialRound = Optional.empty();
    private Partecipant currentPlayer;
    private Table table;
    private boolean gameOver;
    private final List<Partecipant> activePlayers = new ArrayList<>();

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
    public int currentScore(final Hand hand) {
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
             return this.currentScore(p.getHand());
            }
        }
        return 0; 
    }

    @Override
    public void nextTurn() {
        if (table.getCurrentState() == Table.State.PLAYING 
        || table.getCurrentState() == Table.State.FINAL_BET 
        || table.getCurrentState() == Table.State.FIRST_BET) {

         if (currentPlayerIndex < this.activePlayers.size()) { 
            this.currentPlayer = this.activePlayers.get(currentPlayerIndex);
            ++currentPlayerIndex;
         } else {
            this.currentPlayerIndex = 0;

           this.currentPlayer = this.activePlayers.get(currentPlayerIndex);
           ++currentPlayerIndex;

            this.table.stepPassage();

         }
        } else {
            throw new IllegalStateException("impossible to play");
        }

    }

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "Required to keep the View in sync with the real game state."
    )
    @Override
    public void dealerTurn() {
        if (this.table.getCurrentState() == Table.State.DEALER_TURN) { 
                this.currentPlayer = dealer;
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

    @Override
    public void hit() {
        this.deck.draw().ifPresent(this.currentPlayer::addCard);
    }

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "Required to keep the View in sync with the real game state."
    )
    @Override
    public Partecipant getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public boolean isGameOver() {
        if (this.table.getCurrentState() == State.RESULTS) {
            this.gameOver = true;
            return this.gameOver;
        } else {
            return this.gameOver;
        }
    }

    @Override
    public void resetGame() {

        this.activePlayers.clear();
        for (final Partecipant p : players) {
            p.resetHand();
            if (p instanceof Player && ((Player) p).getWallet() > 0) {
                    this.activePlayers.add(p);
            }
        }

        this.dealer.resetHand();
        this.currentPlayerIndex = 0;
        this.gameOver = false;
        this.table.otherGame();
        this.deck.reset();
        this.deck.shuffle();

    }

    @Override
    public void initialCards() {
        for (final Partecipant p : this.activePlayers) {
           if (p instanceof Player && ((Player) p).getCurrentBet() > 0) {
           this.deck.draw().ifPresent(p::addCard);
           this.deck.draw().ifPresent(p::addCard);
           }
        } 
    }

    @Override
    public void initialCardsDealer() {
        this.deck.draw().ifPresent(this.dealer::addCard);
    }

    @Override
    public SpecialRound getSpecialRound() {
        return this.specialRound.orElse(null);
    }

   }
