package it.unibo.chaosjack.controller.api;

/**
 * Interface that provides methos to handle player decisions.
 */
public interface ActionController {

    /**
     * Requests anew card for the current partecipant.
     */
    void hit();

    /**
     * Ends the current partecipant's turn.
     */
    void stand();

    /**
     * places a bet for the current player.
     * 
     * @param amount the amount fot the bet
     */
    void bet(int amount);

    /**
     * doubles the initial bet and gives a card to the partecipant.
     */
    void doubleDown();

    /**
     * Execute the beting phase automatically for the NPC.
     */
    void playAutomatedBet();

    /**
     * Manages automatically resolves the game turn for NPC.
     */
    void playAutomatedTurns();

    /**
     * Executes the dealer's turn.
     */
    void playDealerTurns();
}
