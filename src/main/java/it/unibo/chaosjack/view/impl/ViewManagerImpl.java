package it.unibo.chaosjack.view.impl;

import it.unibo.chaosjack.view.api.GameTableView;
import it.unibo.chaosjack.view.api.MainMenuView;
import it.unibo.chaosjack.view.api.ViewManager;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Implementation of ViewManger's interface.
 */
public class ViewManagerImpl implements ViewManager{
    private final Stage stage;
    private final Scene scene;
    private final MainMenuView mainMenu = new MainMenuViewImpl();
    private final GameTableView gameTable = new GameTableViewImpl();

    public ViewManagerImpl(final Stage stage) {
        this.stage = stage;
        this.scene = new Scene(new Pane(), 800, 600);
        this.stage.setScene(this.scene);
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
        this.mainMenu.setStatsHandler(() -> this.showStatistics());
        this.mainMenu.setExitHandler(() -> System.exit(0));
        
        this.scene.setRoot(this.mainMenu.getRootNode());
        this.stage.setTitle("ChaosJack - Main Menu");
        this.stage.show();
    }

    @Override
    public void showGameTable() {
        //final GameTableView gameTable = new GameTableViewImpl();
        this.gameTable.setMenuHandler(() -> this.showMainMenu());

        this.scene.setRoot(this.gameTable.getRootNode());
        this.stage.setTitle("ChaosJack - Table of Game");
        this.stage.show();
    }

    @Override
    public void showStatistics() {
        this.stage.setTitle("ChaosJack - Statistics");
        this.stage.show();
    }
    
}
