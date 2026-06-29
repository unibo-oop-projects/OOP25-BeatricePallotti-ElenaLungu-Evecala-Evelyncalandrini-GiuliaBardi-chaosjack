package it.unibo.chaosjack;

import javafx.application.Application;

/**
 * The main entry point of the ChaosJack application.
 */
public final class Main {

    /**
     * Private constructor to prevent instantiation of this utility/entry-point class.
     */
    private Main() {

    }

    /**
     * Starts the application.
     * 
     * @param args the command-line arguments passed to the application.
     */
    public static void main(final String[] args) {
        Application.launch(App.class, args);
    }
}
