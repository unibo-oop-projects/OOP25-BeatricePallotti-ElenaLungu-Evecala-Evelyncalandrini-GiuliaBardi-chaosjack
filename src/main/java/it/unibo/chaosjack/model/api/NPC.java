package it.unibo.chaosjack.model.api;

    /**
    * this interface rapresents the NPC that is a specialized player that can make autonomous decisions.
    * without human input
    */

public interface NPC extends Player {

    /**
    * It decides and sets the initial bet for the current round.
    */
     void makeBet();

    /**
     * Decides if the NPC wants to draw another card.
     * 
     * @return true if the NPC should hit
     */
     boolean wantsToHit();

    /**
     * Decides if the NPC wants to double the bet.
     * 
     * @return true if the NPC should double
     */
     boolean wantsToDouble();
}
