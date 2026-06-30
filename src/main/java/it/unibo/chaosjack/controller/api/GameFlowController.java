package it.unibo.chaosjack.controller.api;

/**
 * Model the main flow controller of the game.
 */

public interface GameFlowController {
    /**
     * Allows bots to draw cards.
     */
    void automaticShift();

    /**
     * Manages the game phases.
     */
    void phaseOfGame();

    /**
     * Start a new game.
     */
    void newGame();

    /**
     * Allow bot to bet automatically.
     */
    void automaticBet();
}
