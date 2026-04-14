package it.unibo.chaosjack.model.api;

public interface turnState {
   
    void hit();
    void stand();
    void doubleDown();
    String getStateName();
}
