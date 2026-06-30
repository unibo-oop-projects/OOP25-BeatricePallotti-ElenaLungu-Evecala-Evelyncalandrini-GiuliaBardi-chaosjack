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
      * @param currentScore score of the NPC
      * @return true if the NPC should hit
      */
     boolean wantsToHit(int currentScore);

     /**
      * Decides if the NPC wants to double the bet.
      * 
      * @param currentScore score of the NPC
      * @return true if the NPC should double
      */
     boolean wantsToDouble(int currentScore);
}
