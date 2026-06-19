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
        if (human.isBusted() || engine.currentScore(human.getHand())>= Partecipant.MAX_SCORE) {
            return;
        }
        engine.hit();

        if(human.isBusted() || engine.currentScore(human.getHand())>= Partecipant.MAX_SCORE) {
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
    }

    @Override
    public void bet(int amount) { //devo sistemare questo
        Player human = getCurrentHumanPlayer();
        if (human == null) {
            return;
        }
        try {
           table.placeBet(human.getName(),amount);
           human.setBet(amount);
           engine.stand();
        } catch (IllegalStateException | IllegalArgumentException e) {
            return;
        }
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
        engine.hit();
        this.stand();
    }
    
    private boolean isHumanPlayer(Partecipant p) { //mi serve nei vari metodi per dire sepuò usare i bottoni
        return p instanceof Player && !(p instanceof NPC);
    }

     //metodi  per NPC e Dealer

    @Override
    public void playAutomatedBet() { 
        Partecipant p = engine.getCurrentPlayer();
        if (p instanceof NPC) {
            NPC bot = (NPC) p;
            bot.makeBet();

            table.placeBet(bot.getName(),bot.getCurrentBet());
            engine.stand();
            //this.playAutomatedBet();
            
        }

    }
    @Override
    public void playAutomatedTurns() {

        if(engine.getCurrentPlayer() instanceof NPC) {
            NPC bot = (NPC) engine.getCurrentPlayer();

            if (bot.wantsToDouble()) {
                bot.doubleDown();
                engine.hit();
                engine.stand();
            } else if(bot.wantsToHit()) {
                engine.hit();
            } else {
                engine.stand();
            }
        
        }

    }
    @Override
    public void playDealerTurns() {

       Dealer dealer = (Dealer) engine.getCurrentPlayer();
       if (dealer.shouldHit()) {
           engine.hit(); 
           //this.playDealerTurns(); 
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
