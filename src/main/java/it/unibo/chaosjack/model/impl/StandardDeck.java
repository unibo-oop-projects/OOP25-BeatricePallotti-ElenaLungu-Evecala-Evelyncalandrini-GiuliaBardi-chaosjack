package it.unibo.chaosjack.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.Deck;

/**
 * Implementation of a standard 52-card deck.
 */
public class StandardDeck implements Deck {
    
    private final List<Card> cards;

    public StandardDeck() {
        this.cards = new ArrayList<>();
        // Automatic Generation of all cards.
        for (final Suit suit : Suit.values()) {
            for (final Rank rank : Rank.values()) {
                this.cards.add(new StandardCard(rank, suit));
            }
        }
    }

    @Override
    public Optional<Card> draw() {
        if (this.cards.isEmpty()) {
            return Optional.empty(); 
        }
        // Removes and returns the last card of the list (the top of the pile).
        return Optional.of(this.cards.remove(this.cards.size() - 1));
    }

    @Override
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    @Override
    public int remainingCards() {
        return this.cards.size();
    }
}