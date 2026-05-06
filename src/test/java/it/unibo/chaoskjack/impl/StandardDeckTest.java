package it.unibo.chaoskjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.Deck;
import it.unibo.chaosjack.model.impl.StandardDeck;

import java.util.Optional;

class StandardDeckTest {

    @Test
    void testInitialDeckSize() {
        final Deck deck = new StandardDeck();
        assertEquals(52, deck.remainingCards());
    }

    @Test
    void testDrawCard() {
        final Deck deck = new StandardDeck();
        final Optional<Card> drawnCard = deck.draw();
        
        // Verifica che la carta sia stata pescata con successo
        assertTrue(drawnCard.isPresent());
        // Verifica che il numero di carte rimanenti sia sceso a 51
        assertEquals(51, deck.remainingCards());
    }

    @Test
    void testEmptyDeck() {
        final Deck deck = new StandardDeck();
        
        // Svuotiamo brutalmente tutto il mazzo pescando 52 volte
        for (int i = 0; i < 52; i++) {
            assertTrue(deck.draw().isPresent());
        }
        
        assertEquals(0, deck.remainingCards());
        
        // La prova del 9: peschiamo la 53esima carta. 
        // Il gioco non deve crashare, ma deve dirci che la carta è assente.
        final Optional<Card> impossibleCard = deck.draw();
        assertFalse(impossibleCard.isPresent());
    }
}
