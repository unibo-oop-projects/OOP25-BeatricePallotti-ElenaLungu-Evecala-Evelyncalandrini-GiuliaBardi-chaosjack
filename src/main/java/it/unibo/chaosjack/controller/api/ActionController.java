package it.unibo.chaosjack.controller.api;

public interface ActionController { //metto solo le mosse che l'interfaccia può fare dai bottoni
    
    void hit();

    void stand();
    
    void bet(int amount);

    void doubleDown();
}
