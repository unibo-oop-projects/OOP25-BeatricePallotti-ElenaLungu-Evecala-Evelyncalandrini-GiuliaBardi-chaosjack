package it.unibo.chaosjack.view.api;

import javafx.scene.Parent;


/**
 * Visual interface for a player's wallet (chip balance) component.
 *
 * <p>"Casino Premium" design: dark semi-transparent background, rounded borders,
 * drop shadow and gold/white text for an elegant green-table look.</p>
 */
public interface PlayerWalletView {

    /**
     * Updates the chip balance displayed in the wallet.
     *
     * @param newBalance the new chip balance.
     */
    void updateBalance(int newBalance);

    /**
     * Returns the currently displayed balance string.
     *
     * @return the full balance string.
     */
    String getDisplayedBalance();

    /**
     * @return the root node of the wallet view.
     */
    Parent getRootNode();
}
