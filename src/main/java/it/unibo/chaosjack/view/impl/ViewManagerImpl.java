package it.unibo.chaosjack.view.impl;

import it.unibo.chaosjack.model.api.Statistics;
import it.unibo.chaosjack.view.api.GameTableView;
import it.unibo.chaosjack.view.api.MainMenuView;
import it.unibo.chaosjack.view.api.StatisticsView;
import it.unibo.chaosjack.view.api.ViewManager;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * Implementation of ViewManger's interface.
 */
public class ViewManagerImpl implements ViewManager{
    private final Stage stage;
    private final Scene mainScene;
    private final StackPane rootContainer;
    private Scale currentScale;

    private final MainMenuView mainMenu = new MainMenuViewImpl();
    private final GameTableView gameTable = new GameTableViewImpl();

    private static final double BASE_WIDTH = 1280.0;
    private static final double BASE_HEIGHT = 720.0;

    public ViewManagerImpl(final Stage stage) {
        this.stage = stage;
        this.rootContainer = new StackPane();
        this.mainScene = new Scene(this.rootContainer, BASE_WIDTH, BASE_HEIGHT);
        this.stage.setScene(this.mainScene);
        this.stage.setTitle("ChaosJack");

        this.mainScene.widthProperty().addListener((obs, oldVal, newVal) -> updateScale());
        this.mainScene.heightProperty().addListener((obs, oldVal, newVal) -> updateScale());
    }

    @Override
    public MainMenuView getMainMenu() {
        return this.mainMenu;
    }

    @Override
    public GameTableView getGameTable() {
        return this.gameTable;
    }

    @Override
    public void showMainMenu() {
        /*this.mainMenu.setStatsHandler(() -> {
            StatisticsImpl stats = new StatisticsImpl();
            this.showStatistics(stats);
        });
        this.mainMenu.setExitHandler(() -> System.exit(0));*/

        switchView(this.mainMenu.getRootNode(), "#1a1a1a");
        this.stage.setTitle("ChaosJack - Main Menu");
        if (!this.stage.isShowing()) {
            this.stage.show();
        }
    }

    @Override
    public void showGameTable() {
        this.gameTable.setMenuHandler(() -> this.showMainMenu());
        switchView(this.gameTable.getRootNode(), "#2E8B57");
        this.stage.setTitle("ChaosJack - Table of Game");
        if (!this.stage.isShowing()) {
            this.stage.show();
        }
    }

    @Override
    public void showStatistics(Statistics stats) {
        final StatisticsView statsView = new StatisticsViewImpl();
        Parent statsRoot = statsView.createRoot(stats, () -> {
            this.showMainMenu();
        });

        stage.setTitle("ChaosJack - Statistics");

        
        /*final Scene scene = new Scene(statsView.createRoot(stats, ), 800, 500);
        
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);

        this.stage.show();*/
        switchView(statsRoot, "#2b2b2b");
    }

    private void switchView(Parent rootContent, String backgroundColor) {

        if (rootContent instanceof Region) {
            ((Region) rootContent).setPrefSize(BASE_WIDTH, BASE_HEIGHT);
            ((Region) rootContent).setMinSize(BASE_WIDTH, BASE_HEIGHT);
            ((Region) rootContent).setMaxSize(BASE_WIDTH, BASE_HEIGHT);
        }

        rootContent.getTransforms().clear();
        this.currentScale = new Scale(1, 1, 0, 0);
        rootContent.getTransforms().add(this.currentScale);

        Group group = new Group(rootContent);
        this.rootContainer.getChildren().setAll(group);
        this.rootContainer.setStyle("-fx-background-color: " + backgroundColor + ";");
        updateScale();
    }

    private void updateScale() {
        if (this.currentScale != null) {
            double scaleX = this.mainScene.getWidth() / BASE_WIDTH;
            double scaleY = this.mainScene.getHeight() / BASE_HEIGHT;

            double finalScale = Math.min(scaleX, scaleY);

            this.currentScale.setX(finalScale);
            this.currentScale.setY(finalScale);
        }
    }
    
}
