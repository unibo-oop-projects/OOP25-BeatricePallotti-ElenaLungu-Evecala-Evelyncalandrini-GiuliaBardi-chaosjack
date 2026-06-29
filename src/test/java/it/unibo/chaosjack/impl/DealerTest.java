package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import it.unibo.chaosjack.model.api.Dealer;
import it.unibo.chaosjack.model.impl.DealerImpl;
import it.unibo.chaosjack.model.impl.GameEngineImpl;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.StandardCard;
import it.unibo.chaosjack.model.impl.StandardDeck;
import it.unibo.chaosjack.model.impl.Suit;
import it.unibo.chaosjack.model.api.Deck;
import it.unibo.chaosjack.model.api.GameEngine;

 /**
  * Tests for the class DealerImpl.
  */
 class DealerTest {

    private static final int MAX_SCORE = 21;
    private static final int DEALER_THRESHOLD = 17;

    @Test
    void testDealerMustHit() {
        final Dealer dealer = new DealerImpl();
        dealer.addCard(new StandardCard(Rank.EIGHT, Suit.CLUBS));
        dealer.addCard(new StandardCard(Rank.EIGHT, Suit.DIAMONDS));
        assertTrue(dealer.shouldHit(16), "Il dealer dovrebbe pescare");
    }

    @Test
    void testDealerMustStand() {
        final Dealer dealer = new DealerImpl();
        dealer.addCard(new StandardCard(Rank.QUEEN, Suit.CLUBS));
        dealer.addCard(new StandardCard(Rank.SEVEN, Suit.HEARTS));
        assertFalse(dealer.shouldHit(17), "Il dealer non dovrebbe pescare");

        dealer.resetHand();
        dealer.addCard(new StandardCard(Rank.EIGHT, Suit.DIAMONDS));
        dealer.addCard(new StandardCard(Rank.JACK, Suit.SPADES));
        assertFalse(dealer.shouldHit(18), "Il dealer dovrebbe fermarsi");
    }

    @Test
    void testDealerPlayTurn() {
        final Dealer dealer = new DealerImpl();
        final Deck deck = new StandardDeck();
        final GameEngine engine = new GameEngineImpl(deck, null, dealer);
        dealer.addCard(new StandardCard(Rank.TEN, Suit.CLUBS));
        //dealer.playTurn(deck);
        final int score = engine.currentScore(dealer.getHand());
        assertTrue(score >= DEALER_THRESHOLD, "Il dealer deve finire con almeno 17");

        if (score <= MAX_SCORE) {
            assertTrue(score <= MAX_SCORE, "Il dealer non dovrebbe sballare se le carte glielo permettono");
        }
    }
}
