package it.unibo.chaosjack.model.impl;
import it.unibo.chaosjack.model.api.Wallet;

public class Player {
    private String name;
    private Hand playerHand;
    private boolean isBot;
    private Wallet wallet;
    private int bet;

    public Player ( String name,  boolean isBot, Wallet wallet, int bet) {
        this.name = name;
        this.playerHand = new Hand();
        this.isBot = isBot;
        this.wallet = wallet;
        this.bet = bet;

    }

    /*
    * @return true if the player is a bot, false otherwise
     */
    public boolean isBot() {
       return this.isBot;
    }

    /**
     * @return the name of the player
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return the hand of the player
     */
    public Hand getHand(){
        return this.playerHand;
    }

    /**
     * @return the initial bet of the player
     */
    public int getBet() {
        return this.bet;
    }

    /**
     * @return the wallet of the player
     */
    public Wallet getWallet() {
        return this.wallet;
    }
}
