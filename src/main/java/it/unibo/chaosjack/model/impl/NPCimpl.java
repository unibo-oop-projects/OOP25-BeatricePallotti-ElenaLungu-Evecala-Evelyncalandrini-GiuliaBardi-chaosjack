package it.unibo.chaosjack.model.impl;

import it.unibo.chaosjack.model.api.NPC;
/**
 * Implementation of a Non-Player Character (NPC).
 * This class defines the autonomous behavior of computer-controlled players,
 * including betting strategies and hitting/doubling logic.
 */
public class NPCimpl extends PlayerImpl implements NPC{

    private static final int STANDARD_BET = 10;
    private static final int STOP_THRESHOLD = 15;
    private static final int MIN_DOUBLE_SCORE = 9;
    private static final int MAX_DOUBLE_SCORE = 11;

    /**
     * Constructs a new NPC player
     * @param name
     * @param initialFunds
     */
    public NPCimpl(final String name,final int initialFunds){
       super(name,initialFunds); //passo tutto al costruttore

     }

     /**
      * Executes the NPC's logic
      * It attempts to place a bet of 10.
      * If funds are insufficient it places an "all-in" with the remaining amount
      */
    @Override
    public void makeBet() {

        if(this.getWallet() >= STANDARD_BET){
            this.setBet(STANDARD_BET);
        }
        else{
            this.setBet(this.getWallet());
        }
    } 

    /**
     * Decides if the NPC should hit
     * @return true if it is below 15
     */
    @Override
    public boolean wantsToHit() {
        return this.getScore() < STOP_THRESHOLD;
    }

    /**
     * Decides if the NPC should double the current bet
     * @return true if it is between 9 and 11 inclusive and if there are enough funds
     */
    @Override
    public boolean wantsToDouble(){
        int score = this.getScore();
        return ( score >= MIN_DOUBLE_SCORE && score <= MAX_DOUBLE_SCORE) && (this.getWallet() >= this.getCurrentBet());
    }
}
