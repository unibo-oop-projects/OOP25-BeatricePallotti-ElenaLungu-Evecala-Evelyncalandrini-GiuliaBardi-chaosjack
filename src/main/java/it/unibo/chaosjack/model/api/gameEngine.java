package it.unibo.chaosjack.model.api;
import it.unibo.chaosjack.model.impl.hand;

public interface gameEngine {
    deck getDeck(); // restituisce il mazzo di carte
    hand getPlayerHand(); // restituisce la mano del giocatore
    void changeState(turnState newState);
    hand getDealerHand(); // restituisce la mano del banco
}
