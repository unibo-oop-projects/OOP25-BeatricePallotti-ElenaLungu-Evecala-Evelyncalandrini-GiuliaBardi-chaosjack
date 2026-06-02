package it.unibo.chaosjack.view.api;

import javafx.scene.Parent;

public interface GameOverMenuView {

    Parent getRootNode();

    void setVisible(boolean vsible);
    void setWinnerText(String text);
    void setRestartHanlder(Runnable handler);
    void setExitHandler(Runnable handler);
    
    
}
