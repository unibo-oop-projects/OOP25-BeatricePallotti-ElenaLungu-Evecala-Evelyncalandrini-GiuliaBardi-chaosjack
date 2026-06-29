package it.unibo.chaosjack.view.api;

import javafx.scene.Parent;

/**
 * Interface representing the pause menu view component.
 */
public interface PauseMenuView {

    /**
     * Returns the root node of the pause menu layout.
     * 
     * @return the root node.
     */
    Parent getRootNode();

    /**
     * Changes the visibility status of the pause menu.
     * 
     * @param visible true to show the pause menu, false to hide it.
     */
    void setVisible(boolean visible);

    /**
     * Sets the action handler to be executed when the resume event is triggered.
     * 
     * @param handler the action to resume the current game.
     */
    void setResumeHandler(Runnable handler);

    /**
     * Sets the action handler to be executed when the restart event is triggered.
     * 
     * @param handler the action to restart the game session.
     */
    void setRestartHanlder(Runnable handler);

    /**
     * Sets the action handler to be executed when the exit event is triggered.
     * 
     * @param handler the action to exit back to the main menu.
     */
    void setExitHandler(Runnable handler);

}
