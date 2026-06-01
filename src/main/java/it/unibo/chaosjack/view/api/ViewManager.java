package it.unibo.chaosjack.view.api;

/**
 * The interface for managing game navigation.
 */
public interface ViewManager {

    /**
     * Returns the instance of the main menu view.
     * @return the instance.
     */
    MainMenuView getMainMenu();

    /**
     * Returns the instance of the game table view.
     * @return the instance.
     */
    GameTableView getGameTable();

    /**
     * Main Menu.
     */
    void showMainMenu();

    /**
     * Gaming table.
     */
    void showGameTable();

    /**
     * Statistics and scores.
     */
    void showStatistics();
}
