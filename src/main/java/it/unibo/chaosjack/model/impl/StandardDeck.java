package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.CardModifier;
import it.unibo.chaosjack.model.api.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of a deck that includes special Chaos Jack cards.
 */
public final class StandardDeck implements Deck {

    private static final int SPECIAL_CARD_COUNT = 4;
    private static final int TOTAL_CARDS = 52;

    private final List<Card> cards;

    /**
     * Constructs a new StandardDeck and initializes it with cards.
     */
    public StandardDeck() {
        this.cards = new ArrayList<>();
        this.reset();
    }

    @Override
    public void reset() {
        this.cards.clear();
        final List<CardModifier> modifiers = this.createModifierPool();

        int modIndex = 0;
        for (final Suit suit : Suit.values()) {
            for (final Rank rank : Rank.values()) {
                this.cards.add(new StandardCard(rank, suit, modifiers.get(modIndex)));
                modIndex++;
            }
        }

        this.shuffle();
    }

    @Override
    public Optional<Card> draw() {
        if (this.cards.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.cards.remove(0));
    }

    @Override
    public int remainingCards() {
        return this.cards.size();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    /**
     * Creates a balanced pool of modifiers for the deck.
     *
     * @return a shuffled list of 52 modifiers.
     */
    private List<CardModifier> createModifierPool() {
        final List<CardModifier> pool = new ArrayList<>();

        for (int i = 0; i < SPECIAL_CARD_COUNT; i++) {
            pool.add(CardModifier.BUST_MAGNET);
            pool.add(CardModifier.REVERSE);
            pool.add(CardModifier.GHOST);
        }

        while (pool.size() < TOTAL_CARDS) {
            pool.add(CardModifier.NONE);
        }

        Collections.shuffle(pool);
        return pool;
    }
}
