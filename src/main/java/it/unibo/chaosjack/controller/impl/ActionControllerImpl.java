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

    private static final int SPECIAL_ROUND_MAX_CARDS = 5;

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
        //aggiungo il controllo per le carte massime nei turni speciali
        if (engine.getSpecialRound() != null && human.getHand().getCards().size() >= SPECIAL_ROUND_MAX_CARDS) {
            this.stand();
            return;
        }
        int score = engine.currentScore(human.getHand());
        if (human.isBusted(score) || engine.currentScore(human.getHand())>= Partecipant.MAX_SCORE) {
            return;
        }
        engine.hit();

        if(human.isBusted(score) || engine.currentScore(human.getHand())>= Partecipant.MAX_SCORE) {
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
        
        engine.stand(); 
    }

    @Override
    public void bet(int amount) {
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
        table.placeBet(human.getName(), currentBet);
        human.doubleDown();
        try {
            table.placeBet(human.getName(), currentBet);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return;
        }
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

            int botscore = engine.currentScore(bot.getHand());
            int cardsInHand = bot.getHand().getCards().size();

            if (engine.getSpecialRound() != null && cardsInHand >= SPECIAL_ROUND_MAX_CARDS) {
               engine.stand();
               return;
            }

            if (bot.wantsToDouble(botscore) && cardsInHand == 2) { 
                bot.doubleDown();
                engine.hit();
                engine.stand();
            } else if(bot.wantsToHit(botscore)) {
                engine.hit();
            } else {
                engine.stand();
            }
        
        }

    }
    @Override
    public void playDealerTurns() {

       Dealer dealer = (Dealer) engine.getCurrentPlayer();
       int dealerScore = engine.currentScore(dealer.getHand());
       int cardsInHand = dealer.getHand().getCards().size();

       if (engine.getSpecialRound() != null && cardsInHand >= SPECIAL_ROUND_MAX_CARDS) {
           engine.stand();
           return;
       }
       if (dealer.shouldHit(dealerScore)) {
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
