package it.unibo.chaosjack.view.impl;

import it.unibo.chaosjack.view.api.PauseMenuView;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Implementation of pause button and pause screen.
 */
public final class PauseMenuViewImpl implements PauseMenuView {

    private static final int BOX_SPACING = 20;

    private final StackPane root;
    private final VBox menuBox;
    private final Button resumeButton = new Button("Resume");
    private final Button restartButton = new Button("Restart");
    private final Button exitButton = new Button("Menu");

    /**
     * Initializes the pause menu view, configuring background, the title label and the action buttons.
     */
    public PauseMenuViewImpl() {
        this.root = new StackPane();
        this.root.setAlignment(Pos.CENTER);
        this.root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75);");
        this.root.setVisible(false);

        this.root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        this.menuBox = new VBox(BOX_SPACING);
        this.menuBox.setAlignment(Pos.CENTER);

        final Label title = new Label("PAUSE");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold; -fx-padding: 0 0 20 0;");

        final String btnStyle = "-fx-font-size: 18px; -fx-padding: 10 30; -fx-min-width: 220px;";
        resumeButton.setStyle(btnStyle + "-fx-base: #28a745");
        restartButton.setStyle(btnStyle + "-fx-base: #007bff");
        exitButton.setStyle(btnStyle + "-fx-base: #dc3545");

        this.menuBox.getChildren().addAll(title, resumeButton, restartButton, exitButton);
        this.root.getChildren().add(this.menuBox);
    }

    @Override
    public Parent getRootNode() {
        return this.root;
    }

    @Override
    public void setVisible(final boolean visible) {
        Platform.runLater(() -> this.root.setVisible(visible));
    }

    @Override
    public void setResumeHandler(final Runnable handler) {
        this.resumeButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setRestartHanlder(final Runnable handler) {
        this.restartButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setExitHandler(final Runnable handler) {
       this.exitButton.setOnAction(e -> handler.run());
    }

}
