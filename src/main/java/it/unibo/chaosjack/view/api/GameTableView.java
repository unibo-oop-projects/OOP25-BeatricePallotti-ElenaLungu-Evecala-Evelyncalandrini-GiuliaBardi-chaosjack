package it.unibo.chaosjack.view.api;

import java.util.function.Consumer;

import it.unibo.chaosjack.model.api.Table;
import javafx.scene.Parent;

/**
 * Represents the visual interface of the game table.
 */
public interface GameTableView {

    /**
     * @return the root node.
     */
    Parent getRootNode();

    /**
     * The current table state.
     * @param state .
     */
    void setGameState(Table.State state);

    /**
     * @param amount shows the amount of fish in the pot.
     */
    void updatePot(int amount);

    /**
     * The action of click the button "Hit".
     * @param handler the action.
     */
    void setHitHandler(Runnable handler);

    /**
     * The action of click the button "Stand".
     * @param handler the action.
     */
    void setStandHandler(Runnable handler);

    /**
     * The action of click the button "DoubleHandler".
     * @param handler the action.
     */
    void setDoubleDownHandler(Runnable handler);

    /**
     * The action of click the button "Bet".
     * @param handler the action.
     */
    void setBetHandler(Consumer<Integer> handler);

    /**
     * Navigaton in the menu.
     * @param handler .
     */
    void setMenuHandler(Runnable handler);
}
