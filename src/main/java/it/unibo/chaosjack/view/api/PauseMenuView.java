package it.unibo.chaosjack.view.api;

import javafx.scene.Parent;

public interface PauseMenuView {

    /**
     * @return the root node.
     */
    Parent getRootNode();

    /**
     * @param visible
     */
    void setVisible(boolean visible);

    /**
     * 
     * @param handler
     */
    void setResumeHandler(Runnable handler);

    /**
     * 
     * @param handler
     */
    void setRestartHanlder(Runnable handler);

    /**
     * 
     * @param handler
     */
    void setExitHandler(Runnable handler);
    
}
