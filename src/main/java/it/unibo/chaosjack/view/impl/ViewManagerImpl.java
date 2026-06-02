package it.unibo.chaosjack.view.impl;

import it.unibo.chaosjack.view.api.GameTableView;
import it.unibo.chaosjack.view.api.MainMenuView;
import it.unibo.chaosjack.view.api.ViewManager;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * Implementation of ViewManger's interface.
 */
public class ViewManagerImpl implements ViewManager{
    private final Stage stage;
    private final Scene menuScene;
    private final Scene gameScene;
    private final MainMenuView mainMenu = new MainMenuViewImpl();
    private final GameTableView gameTable = new GameTableViewImpl();

    public ViewManagerImpl(final Stage stage) {
        this.stage = stage;
        this.gameScene = createScene(this.gameTable.getRootNode(), "#2E8B57");
        this.menuScene = createScene(this.mainMenu.getRootNode(), "#1a1a1a");
        this.stage.setTitle("ChaosJack");
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
        /*final MainMenuView menuView = new MainMenuViewImpl();
        menuView.setPlayHandler(() -> this.showGameTable());
        menuView.setStatsHandler(() -> this.showStatistics());
        menuView.setExitHandler(() -> System.exit(0));*/
        //this.mainMenu.setPlayHandler(() -> this.showGameTable());
        this.mainMenu.setStatsHandler(() -> this.showStatistics());
        this.mainMenu.setExitHandler(() -> System.exit(0));
        
        //this.scene.setRoot(this.mainMenu.getRootNode());
    
        this.stage.setScene(this.menuScene);
        this.stage.setTitle("ChaosJack - Main Menu");
        this.stage.show();
    }

    @Override
    public void showGameTable() {
        //final GameTableView gameTable = new GameTableViewImpl();
        this.gameTable.setMenuHandler(() -> this.showMainMenu());
        this.stage.setScene(this.gameScene);

        //this.scene.setRoot(this.gameTable.getRootNode());
        this.stage.setTitle("ChaosJack - Table of Game");
        this.stage.show();
    }

    @Override
    public void showStatistics() {
        this.stage.setTitle("ChaosJack - Statistics");
        this.stage.show();
    }

    private Scene createScene(Parent rootContent, String backgroundColor) {
        final double BASE_WIDTH = 1280.0;
        final double BASE_HEIGHT = 720.0;

        if (rootContent instanceof Region) {
            ((Region) rootContent).setPrefSize(BASE_WIDTH, BASE_HEIGHT);
            ((Region) rootContent).setMinSize(BASE_WIDTH, BASE_HEIGHT);
            ((Region) rootContent).setMaxSize(BASE_WIDTH, BASE_HEIGHT);
        }

        rootContent.getTransforms().clear();
        Scale scale = new Scale(1, 1, 0, 0);
        rootContent.getTransforms().add(scale);

        Group group = new Group(rootContent);
        StackPane wrapper = new StackPane(group);
        wrapper.setStyle("-fx-background-color:" + backgroundColor + ";");

        Scene scene = new Scene(wrapper, BASE_WIDTH, BASE_HEIGHT);

        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> {
            double scaleX = scene.getWidth() / BASE_WIDTH;
            double scaleY = scene.getHeight() / BASE_HEIGHT;

            double finalScale = Math.min(scaleX, scaleY);

            scale.setX(finalScale);
            scale.setY(finalScale);
        };

        scene.widthProperty().addListener(sizeListener);
        scene.heightProperty().addListener(sizeListener);

        return scene;
    }
    
}
