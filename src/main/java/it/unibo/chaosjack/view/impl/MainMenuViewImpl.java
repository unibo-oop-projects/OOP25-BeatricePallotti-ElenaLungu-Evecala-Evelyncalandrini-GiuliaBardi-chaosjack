package it.unibo.chaosjack.view.impl;

import it.unibo.chaosjack.view.api.MainMenuView;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Implementation of Main Menu View.
 */
public class MainMenuViewImpl implements MainMenuView {
    private final VBox root;
    private final Button playButton = new Button("Play");
    private final Button statsButton = new Button("Statistics");
    private final Button exitButton = new Button("Exit");

    private final Label nameLabel = new Label("Name: ");
    private final TextField nameField = new TextField("Giocatore 1");

    public MainMenuViewImpl() {
        this.root = new VBox(25);
        this.initLayout();
    }

    private void initLayout() {
        this.root.setAlignment(Pos.CENTER);
        this.root.setStyle("-fx-background-color: #1a1a1a;");

        final Label title = new Label("CHAOS JACK");
        title.setStyle("-fx-text-fill: gold; -fx-font-size: 46px; -fx-font-weight: bold;");

        this.nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        this.playButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 25;");
        this.statsButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 25;");
        this.exitButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 25;");

        this.root.getChildren().addAll(title, playButton, statsButton, exitButton);
    }

    @Override
    public Parent getRootNode() {
        return this.root;
    }

    @Override
    public String getPlayerName() {
        final String inputName = this.nameField.getText().trim();
        return inputName.isEmpty() ? "Giocatore 1" : inputName;
    }

    @Override
    public void setPlayHandler(Runnable handler) {
        this.playButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setStatsHandler(Runnable handler) {
        this.statsButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setExitHandler(Runnable handler) {
        this.exitButton.setOnAction(e -> handler.run());
    }

}
