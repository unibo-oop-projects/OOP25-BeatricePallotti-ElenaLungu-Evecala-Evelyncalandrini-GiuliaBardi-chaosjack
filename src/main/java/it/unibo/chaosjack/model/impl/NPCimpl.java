package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.NPC;

/**
 * Implementation of a Non-Player Character (NPC).
 * This class defines the autonomous behavior of computer-controlled players,
 * including betting strategies and hitting/doubling logic.
 */

public final class NPCimpl extends PlayerImpl implements NPC {

    private static final int STANDARD_BET = 10;
    private static final int STOP_THRESHOLD = 15;
    private static final int MIN_DOUBLE_SCORE = 9;
    private static final int MAX_DOUBLE_SCORE = 11;

    /**
     * Constructs a new NPC player.
     * 
     * @param name the name of the NPC
     * @param initialFunds the initial amount of money for the NPC
     */
    public NPCimpl(final String name, final int initialFunds) {
       super(name, initialFunds); 

     }

    @Override
    public void makeBet() {
        if (this.getWallet() >= STANDARD_BET) {
            this.setBet(STANDARD_BET);
        } else {
            this.setBet(this.getWallet());
        }
    } 

    @Override
    public boolean wantsToHit() {
        return this.getHand().getScore() < STOP_THRESHOLD;
    }

    @Override
    public boolean wantsToDouble() {
        final int score = this.getHand().getScore();
        return score >= MIN_DOUBLE_SCORE && score <= MAX_DOUBLE_SCORE && this.getWallet() >= this.getCurrentBet();
    }
}
