package it.unibo.chaosjack.view.api;

import java.util.List;
import java.util.function.Consumer;

import it.unibo.chaosjack.model.api.Card;
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
     * Enables or disables the action buttons for the player (e.g., Hit, Stand, Double Down).
     * @param disable true to disable the buttons, false to enable them.
     */
    void setPlayerButtons(final boolean disable);

    /**
     * Enables or disables the betting buttons (e.g., fishes for 10, 50, 100).
     * @param disable true to disable the buttons, false to enable them.
     */
    void setBetButton(final boolean disable);

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

    /**
     * To see if we are in a special round.
     * @param ruleName name of round.
     */
    void setSpecialRound(String ruleName);

    /**
     * Show whose turn it is.
     * @param activeName player's name or dealer.
     */
    void setActiveTurn(String activeName);

    /**
     * Update grafic of dealer's cards.
     * @param cards .
     */
    void updateDealerCard(final List<Card> cards);

    /**
     * Update grafic of first player's  cards.
     * @param cards .
     */
    void updatePlayer1Cards(final List<Card> cards);

    /**
     * Update grafic of second player's cards.
     * @param cards
     */
    void updatePlayer2Cards(final List<Card> cards);

    /**
     * Dynamically assigns names to players.
     * @param name1 first player.
     * @param name2 second player.
     */
    void setPlayerNames(final String name1, final String name2);
}
