package it.unibo.chaosjack.view.api;

/**
 * Visual interface for a player's wallet (chip balance) component.
 *
 * <p>"Casino Premium" design: dark semi-transparent background, rounded borders,
 * drop shadow and gold/white text for an elegant green-table look.</p>
 */
public interface PlayerWalletView {

    /**
     * Sets the player name displayed in the wallet.
     *
     * @param name the player's name (will be converted to uppercase).
     */
    void setPlayerName(String name);

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
     * Returns the currently displayed player name.
     *
     * @return the name string (uppercase).
     */
    String getDisplayedName();
}
