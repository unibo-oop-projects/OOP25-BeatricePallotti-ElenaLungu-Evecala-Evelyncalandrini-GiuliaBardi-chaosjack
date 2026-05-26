package it.unibo.chaosjack.controller.impl;


import it.unibo.chaosjack.controller.api.ActionController;
import it.unibo.chaosjack.model.api.Dealer;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.NPC;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.Partecipant;

public class ActionControllerImpl implements ActionController{
    
    private final Table table;
    private final GameEngine engine;

    public ActionControllerImpl(final Table table , final GameEngine engine) {
        this.table = table;
        this.engine = engine;
        
    }

    @Override
    public void hit() {
        if (table.getCurrentState() != Table.State.PLAYING) {
            return;
        }
        Player human = getCurrentHumanPlayer();
        if (human == null) {
            return;
        }
        if (human.isBusted() || human.getHand().getScore()>= Partecipant.MAX_SCORE) {
            return;
        }
        engine.getDeck().draw().ifPresent(card -> human.getHand().addCard(card));

        if(human.isBusted() || human.getHand().getScore()>= Partecipant.MAX_SCORE) {
            this.stand();
        }
    }

    @Override
    public void stand() {
        if (table.getCurrentState() != Table.State.PLAYING) {
            return;
        }

        Player human = getCurrentHumanPlayer();
        if (human == null) {
            return;
        }
        
        engine.stand(); //faccio effettivamente stand
        this.playAutomatedTurns();
    }

    @Override
    public void bet(int amount) {
        if(table.getCurrentState() != Table.State.FIRST_BET && table.getCurrentState() != Table.State.FINAL_BET) {
            return; //se non siamo in una delle due fasi di scommese non le posso fare
        }

        Player human = getCurrentHumanPlayer();
        if (human == null) {
            return;
        }
        if(human.getWallet() < amount) {
            return;
        }
        human.setBet(amount);
        engine.stand();
        this.playAutomatedBet(); //passo alle scommesse dell'NPC
        
    }

    @Override
    public void doubleDown() {
        if(table.getCurrentState() != Table.State.PLAYING) {
            return;
        }
        Player human = getCurrentHumanPlayer();
        if (human == null) {
            return;
        }
        if(human.getHand().getCards().size() != 2) {
            return;
        }
        int currentBet = human.getCurrentBet();
        if(human.getWallet() < currentBet) {
            return;
        }
        human.doubleDown();
        engine.getDeck().draw().ifPresent(card -> human.getHand().addCard(card));
        this.stand();

    }

    private boolean isHumanPlayer(Partecipant p) { //mi serve nei vari metodi per dire sepuò usare i bottoni
        return p instanceof Player && !(p instanceof NPC);
    }

     //metodi privati per NPC e Dealer

    @Override
    public void playAutomatedBet() { 
         while(engine.getCurrentPlayer() instanceof NPC) {
            NPC bot = (NPC) engine.getCurrentPlayer();

            bot.makeBet();
            engine.stand();
            
        }

    }
    @Override
    public void playAutomatedTurns() {

        while(engine.getCurrentPlayer() instanceof NPC) {
            NPC bot = (NPC) engine.getCurrentPlayer();

            if (bot.wantsToDouble()) {
                bot.doubleDown();
                engine.getDeck().draw().ifPresent(card -> bot.getHand().addCard(card));
                engine.stand();
                continue;
            }
            while(bot.wantsToHit()) {
                engine.getDeck().draw().ifPresent(card -> bot.getHand().addCard(card));
            }

            engine.stand();
        }

    }
    @Override
    public void playDealerTurns() {

       Dealer dealer = (Dealer) engine.getCurrentPlayer();
       if (dealer.shouldHit()) {
           engine.hit();  
       } else {
           engine.stand();
       }

    }
    //metodo per il player umano per non fare troppe ripetizioni
    private Player getCurrentHumanPlayer() {
        Partecipant current = engine.getCurrentPlayer();
        if(isHumanPlayer(current)) {
            return (Player) current;
        }
        return null;
    }
} 
