package it.unibo.chaosjack.model.impl;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.Deck;
import it.unibo.chaosjack.model.api.TurnState;
import java.util.List;
import java.util.Optional;
import it.unibo.chaosjack.model.api.SpecialRound;

public class GameEngineImpl implements GameEngine {

    private Deck deck;
    private Hand dealerHand;
    private List<Player> players;
    //private Wallet walletPlayer;
    private TurnState currentState;
    private int currentPlayerIndex = -1;
    private Optional<SpecialRound> specialRound = Optional.empty();

    public GameEngineImpl( Deck deck, List<Player> players) { // ricorda di aggiuungere il wallet
        this.deck = deck;
        this.players = players;
        this.dealerHand = new Hand();

        this.nextTurn();
    }

    /**
     * @param specialRound
     * this method allows to set a special round, if the parameter is null the special round is removed 
     * and the game returns to normal
     */
    public void setSpecialRound(SpecialRound  specialRound){
        this.specialRound = Optional.ofNullable(specialRound);
    }

    /**
     * @param hand
     * @return This method calculates the score following the rules of the current shift, 
     * if a special shift is active it will calculate the score based on the shift rules,
     *  otherwise it will calculate it normally
    */
    public int currentScore(Hand hand){
        if(this.specialRound.isPresent()){
            return this.specialRound.get().specialScore(hand.getCards());
        } else {
            return hand.getScore();
        }
    }
    

    /**
     * this method allows to change the state of the game (switch from player's turn to dealer's turn and vice versa)
     */
    @Override
    public void changeState(TurnState newState){
        this.currentState = newState; // questo metodo mi permette di cambiare lo stato del gioco (passare dal turno del giocatore a quello del banco e viceversa)
    }

    /**
     * @return the deck of the game
     */
    @Override
    public Deck getDeck() {
        return deck;
    }

    
    @Override
    public Hand getDealerHand() {
        return dealerHand;
    }

    

    /**
     * @param name is the game player's name
     * @return the score of the player with the given name, if there is no player with that name it returns 0
     */
    public int getPlayerScore(String name) { 
        for ( Player p : players) {
           if ( p.getName().equals(name) ) {
             return p.getHand().getScore();
           }
        }
        return 0; 
    }


    /**
     * This method handles turn switching between players whether they are bot or not, 
     */
    @Override
    public void nextTurn() {
       currentPlayerIndex++; 
         if (currentPlayerIndex < players.size()) { 

            Player nextPlayer = players.get(currentPlayerIndex);
            if (nextPlayer.isBot()) { // controllo che sia effettivamente presente il giocatore 
                this.changeState(new BotTurn(this, currentPlayerIndex));
            } else {
                this.changeState(new PlayerTurn(this, currentPlayerIndex)); // passo al turno del giocatore successivo
            } 
            
        } 
        /**
         if there aren't other players, the turn passes to the dealer
        */
         else {
            this.changeState(new DealerTurn(this)); // se non ci sono più giocatori passo al turno del banco
        }
    }

    /**
     * @return the list of players in the game
     */
    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void hit(){
        this.currentState.hit();
    }

    @Override
    public void stand(){
        this.currentState.stand();
    }
   }


