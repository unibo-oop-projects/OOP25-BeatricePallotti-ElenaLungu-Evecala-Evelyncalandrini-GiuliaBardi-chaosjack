package it.unibo.chaosjack.view.api;

import javafx.scene.Parent;

/**
 * Interface for the Main Menu View.
 */
public interface MainMenuView {

    /**
     * Returns the root node of the main menu layout.
     * 
     * @return the root node of the menu.
     */
    Parent getRootNode();

    /**
     * Retrieves the username entered by the player in the input field.
     * 
     * @return the name inserted by player as a string.
     */
    String getPlayerName();

    /**
     * Sets the action for the play button click.
     * 
     * @param handler the action.
     */
    void setPlayHandler(Runnable handler);

    /**
     * Sets the action for the statistics button click.
     * 
     * @param handler the action.
     */
    void setStatsHandler(Runnable handler);

    /**
     * Sets the action for the exit button click.
     * 
     * @param handler the action.
     */
    void setExitHandler(Runnable handler);

}
