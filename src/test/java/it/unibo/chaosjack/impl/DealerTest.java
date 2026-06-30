package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import it.unibo.chaosjack.model.api.Dealer;
import it.unibo.chaosjack.model.impl.DealerImpl;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.StandardCard;
import it.unibo.chaosjack.model.impl.Suit;

 /**
  * Tests for the class DealerImpl.
  */
 class DealerTest {

    private static final int DEALER_THRESHOLD = 17;
    private static final int SCORE_TO_HIT = 16;
    private static final int SCORE_TO_STAND = 18;

    @Test
    void testDealerMustHit() {
        final Dealer dealer = new DealerImpl();
        dealer.addCard(new StandardCard(Rank.EIGHT, Suit.CLUBS));
        dealer.addCard(new StandardCard(Rank.EIGHT, Suit.DIAMONDS));
        assertTrue(dealer.shouldHit(SCORE_TO_HIT), "Il dealer dovrebbe pescare");
    }

    @Test
    void testDealerMustStand() {
        final Dealer dealer = new DealerImpl();
        dealer.addCard(new StandardCard(Rank.QUEEN, Suit.CLUBS));
        dealer.addCard(new StandardCard(Rank.SEVEN, Suit.HEARTS));
        assertFalse(dealer.shouldHit(DEALER_THRESHOLD), "Il dealer non dovrebbe pescare");

        dealer.resetHand();
        dealer.addCard(new StandardCard(Rank.EIGHT, Suit.DIAMONDS));
        dealer.addCard(new StandardCard(Rank.JACK, Suit.SPADES));
        assertFalse(dealer.shouldHit(SCORE_TO_STAND), "Il dealer dovrebbe fermarsi");
    }
}
