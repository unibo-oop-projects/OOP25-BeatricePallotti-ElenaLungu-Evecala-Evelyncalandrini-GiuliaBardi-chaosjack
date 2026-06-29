package it.unibo.chaosjack.view.api;

import it.unibo.chaosjack.model.api.Statistics;

/**
 * The interface for managing game navigation.
 */
public interface ViewManager {

    /**
     * Returns the instance of the main menu view.
     * 
     * @return the instance.
     */
    MainMenuView getMainMenu();

    /**
     * Returns the instance of the game table view.
     * 
     * @return the instance.
     */
    GameTableView getGameTable();

    /**
     * Screen Main Menu.
     */
    void showMainMenu();

    /**
     * Screen Gaming table.
     */
    void showGameTable();

    /**
     * Statistics and scores.
     * 
     * @param stats are game's statistics.
     */
    void showStatistics(Statistics stats);
}
