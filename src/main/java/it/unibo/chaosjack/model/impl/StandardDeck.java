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
public final class StandardDeck implements Deck {

    private final List<Card> cards;

    /**
     * Constructs a new standard deck and generates all 52 cards.
     */
    public StandardDeck() {
        this.cards = new ArrayList<>();
        this.reset();
    }

    @Override
    public void reset() {
        this.cards.clear();
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