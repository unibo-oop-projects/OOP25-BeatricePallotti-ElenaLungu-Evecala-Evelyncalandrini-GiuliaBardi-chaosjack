package it.unibo.chaosjack.view.api;

import javafx.scene.Parent;

/**
 * Interface for the Main Menu View.
 */
public interface MainMenuView {
    
    /**
     * @return node of the menu.
     */
    Parent getRootNode();

    /**
     * @return the name inserted by player.
     */
    String getPlayerName();

    /**
     * Sets the action for the play button click.
     * @param handler the action.
     */
    void setPlayHandler(Runnable handler);

    /**
     * Sets the action for the statistics button click.
     * @param handler the action.
     */
    void setStatsHandler(Runnable handler);

    /**
     * Sets the action for the exit button click.
     * @param handler the action.
     */
    void setExitHandler(Runnable handler);

}
