package it.unibo.chaosjack.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import it.unibo.chaosjack.model.api.NPC;
import it.unibo.chaosjack.model.impl.NPCimpl;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.StandardCard;
import it.unibo.chaosjack.model.impl.Suit;

 /**
  * Tests for the NPCImpl class.
  */
 class NPCTest {

    private static final int SCORE_TO_HIT = 10;
    private static final int SCORE_TO_STAND = 19;
    private static final int SCORE_DOUBLE = 11;
    private static final int SCORE_NO_DOUBLE = 14;

    @Test
    void testMakeBet() {
        final int initialFunds = 100;
        final int expectedBet = 10;
        final NPC npc = new NPCimpl("bot-1", initialFunds);
        npc.makeBet();
        assertEquals(expectedBet, npc.getCurrentBet(), "La scommessa dovrebbe essere 10");
    }

    @Test
    void testMakeBetWithLowFunds() {
        final int initialFunds = 5;
        final int expectedBet = 5;
        final NPC npc = new NPCimpl("bot-2", initialFunds);
        npc.makeBet();
        assertEquals(expectedBet, npc.getCurrentBet(), "La scommessa dobrebbe essere 5 perchè fa all-in");
    }

    @Test
    void testHitStrategy() {
        final int initialFunds = 100;
        final NPC npc = new NPCimpl("bot-3", initialFunds);
        npc.addCard(new StandardCard(Rank.SIX, Suit.CLUBS));
        npc.addCard(new StandardCard(Rank.FOUR, Suit.HEARTS));
        assertTrue(npc.wantsToHit(SCORE_TO_HIT), "L'NPC dovrebbe chiedere carta con un punteggio di 10");
        npc.resetHand();
        npc.addCard(new StandardCard(Rank.KING, Suit.SPADES));
        npc.addCard(new StandardCard(Rank.NINE, Suit.DIAMONDS));
        assertFalse(npc.wantsToHit(SCORE_TO_STAND), "L'NPC non dovrebbe chiedere carta con 19");
    }

    @Test
    void testShouldDouble() {
        final int initialFunds = 100;
        final NPC npc = new NPCimpl("bot-4", initialFunds);
        npc.addCard(new StandardCard(Rank.FIVE, Suit.SPADES));
        npc.addCard(new StandardCard(Rank.SIX, Suit.CLUBS));
        assertTrue(npc.wantsToDouble(SCORE_DOUBLE), "L'NPC dovrebbe raddoppiare con 11");

        npc.resetHand();
        npc.addCard(new StandardCard(Rank.JACK, Suit.HEARTS));
        npc.addCard(new StandardCard(Rank.FOUR, Suit.SPADES));
        assertFalse(npc.wantsToDouble(SCORE_NO_DOUBLE), "L'NPC non dovrebbe raddoppaire con 14");
    }
}
