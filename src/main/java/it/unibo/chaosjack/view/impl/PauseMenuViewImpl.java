package it.unibo.chaosjack.view.impl;

import it.unibo.chaosjack.view.api.PauseMenuView;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PauseMenuViewImpl implements PauseMenuView {

    private final VBox root;
    private final Button resumeButton = new Button("Resume");
    private final Button restartButton = new Button("Restart");
    private final Button exitButton = new Button("Menu");

    public PauseMenuViewImpl() {
        this.root = new VBox(20);
        this.root.setAlignment(Pos.CENTER);
        this.root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75);");
        this.root.setVisible(false);
        
        Label title = new Label("PAUSE");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold; -fx-padding: 0 0 20 0;");
        
        String btnStyle = "-fx-font-size: 18px; -fx-padding: 10 30; -fx-min-width: 220px;";
        resumeButton.setStyle(btnStyle + "-fx-base: #28a745");
        restartButton.setStyle(btnStyle + "-fx-base: #007bff");
        exitButton.setStyle(btnStyle + "-fx-base: #dc3545");

        this.root.getChildren().addAll(title, resumeButton, restartButton, exitButton);
    }

    @Override
    public Parent getRootNode() {
        return this.root;
    }

    @Override
    public void setVisible(boolean visible) {
        Platform.runLater(() -> this.root.setVisible(visible));
    }

    @Override
    public void setResumeHandler(Runnable handler) {
        this.resumeButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setRestartHanlder(Runnable handler) {
        this.restartButton.setOnAction(e -> handler.run());
    }
    @Override
    public void setExitHandler(Runnable handler) {
       this.exitButton.setOnAction(e -> handler.run());
    }
    
}
