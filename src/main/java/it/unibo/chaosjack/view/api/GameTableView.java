package it.unibo.chaosjack.view.api;

import java.util.List;
import java.util.function.Consumer;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.Table;
import javafx.scene.Parent;

/**
 * Represents the visual interface of the game table.
 */
public interface GameTableView extends GameScoreDisplay {

    /**
     * Returns the root node of the game table layout.
     * 
     * @return the root node of this view.
     */
    Parent getRootNode();

    /**
     * The current table state.
     * 
     * @param state of the game.
     */
    void setGameState(Table.State state);

    /**
     * Enables or disables the action buttons for the player (e.g., Hit, Stand, Double Down).
     * 
     * @param disable true to disable the buttons, false to enable them.
     */
    void setPlayerButtons(boolean disable);

    /**
     * Enables or disables the betting buttons (e.g., fishes for 10, 50, 100).
     * 
     * @param disable true to disable the buttons, false to enable them.
     */
    void setBetButton(boolean disable);

    /**
     * Updates the total pot amount displayed on the game table interface.
     * 
     * @param amount shows the amount of fish in the pot.
     */
    void updatePot(int amount);

    /**
     * The action of click the button "Hit".
     * 
     * @param handler the action.
     */
    void setHitHandler(Runnable handler);

    /**
     * The action of click the button "Stand".
     * 
     * @param handler the action.
     */
    void setStandHandler(Runnable handler);

    /**
     * The action of click the button "DoubleHandler".
     * 
     * @param handler the action.
     */
    void setDoubleDownHandler(Runnable handler);

    /**
     * The action of click the button "Bet".
     * 
     * @param handler the action.
     */
    void setBetHandler(Consumer<Integer> handler);

    /**
     * Navigaton in the menu.
     * 
     * @param handler .
     */
    void setMenuHandler(Runnable handler);

    /**
     * Gets the pause menu view sub-component.
     * 
     * @return the pause menu view.
     */
    PauseMenuView getPauseMenu();

    /**
     * Sets the handler for the pause button actions.
     * 
     * @param handler the acton to run upon pausing.
     */
    void setPauseHandler(Runnable handler);

    /**
     * To see if we are in a special round.
     * 
     * @param ruleName name of round.
     */
    void setSpecialRound(String ruleName);

    /**
     * Show whose turn it is.
     * 
     * @param activeName player's name or dealer.
     */
    void setActiveTurn(String activeName);

    /**
     * Update grafic of dealer's cards.
     * 
     * @param cards .
     */
    void updateDealerCard(List<Card> cards);

    /**
     * Update grafic of first player's cards.
     * 
     * @param cards .
     */
    void updatePlayer1Cards(List<Card> cards);

    /**
     * Update grafic of second player's cards.
     * 
     * @param cards .
     */
    void updatePlayer2Cards(List<Card> cards);

    /**
     * Show the result of game.
     * 
     * @param resultMessage .
     */
    void showResult(String resultMessage);

    /**
     * Reset tablew view for new game.
     */
    void resetTable();

    /**
     * Dynamically assigns names to players.
     * 
     * @param name1 first player.
     * @param name2 second player.
     */
    void setPlayerNames(String name1, String name2);
}
