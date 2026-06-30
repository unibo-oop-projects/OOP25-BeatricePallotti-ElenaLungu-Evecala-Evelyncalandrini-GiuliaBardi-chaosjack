package it.unibo.chaosjack.controller.impl;

import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.chaosjack.controller.api.ActionController;
import it.unibo.chaosjack.model.api.Dealer;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.NPC;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.Partecipant;

/**
 * Implementation of the {@link ActionController} interface.
 * This class manages game actions,actions between the Table and GameEngine 
 */
public final class ActionControllerImpl implements ActionController {

    private static final int MAX_CARDS_ALLOWED = 5;
    private final Table table;
    private final GameEngine engine;

    /**
     * Constructs a new ActionControllerImpl.
     * 
     * @param table the current game table
     * @param engine the game engine managing the rules
     */

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "The MVC's controller has to share the mutable references to Table and Engine."
    )
    public ActionControllerImpl(final Table table, final GameEngine engine) {
        this.table = table;
        this.engine = engine;

    }

    @Override
    public void hit() {
        if (table.getCurrentState() != Table.State.PLAYING) {
            return;
        }
        final Player human = getCurrentHumanPlayer();
        if (human == null) {
            return;
        }
        if (human.getHand().getCards().size() >= MAX_CARDS_ALLOWED) {
            this.stand();
            return;
        }
        int score = engine.currentScore(human.getHand());
        if (human.isBusted(score) || score >= Partecipant.MAX_SCORE) {
            return;
        }
        engine.hit();
        score = engine.currentScore(human.getHand());
        if (human.isBusted(score) || score >= Partecipant.MAX_SCORE) {
            this.stand();
        }
    }

    @Override
    public void stand() {
        if (table.getCurrentState() != Table.State.PLAYING) {
            return;
        }

        final Player human = getCurrentHumanPlayer();
        if (human == null) {
            return;
        }
        engine.stand(); 
    }

    @Override
    public void bet(final int amount) {
        final Player human = getCurrentHumanPlayer();
        if (human == null) {
            return;
        }
        if (human.getWallet() < amount) {
            return;
        }
        if (table.getCurrentState() == Table.State.FIRST_BET || table.getCurrentState() == Table.State.FINAL_BET) {
            table.placeBet(human.getName(), amount);
            human.setBet(amount);
            engine.stand();
        }
    }

    @Override
     public void doubleDown() {
       if (table.getCurrentState() != Table.State.PLAYING) {
           return;
        }
        final Player human = getCurrentHumanPlayer();
        if (human == null) {
            return;
        }
        if (human.getHand().getCards().size() != 2) {
           return;
        }
        final int currentBet = human.getCurrentBet();
        if (human.getWallet() < currentBet) {
          return;
        }
        human.doubleDown();
        try {
            table.placeBet(human.getName(), currentBet);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return;
        }
        engine.hit();
        this.stand();
    }

    private boolean isHumanPlayer(final Partecipant p) {
        return p instanceof Player && !(p instanceof NPC);
    }

    @Override
    public void playAutomatedBet() {
        final Partecipant p = engine.getCurrentPlayer();
        if (p instanceof NPC) {
            final NPC bot = (NPC) p;
            bot.makeBet();

            table.placeBet(bot.getName(), bot.getCurrentBet());
            engine.stand();

        }

    }

    @Override
    public void playAutomatedTurns() {

        if (engine.getCurrentPlayer() instanceof NPC) {
            final NPC bot = (NPC) engine.getCurrentPlayer();

            final int botscore = engine.currentScore(bot.getHand());
            final int cardsInHand = bot.getHand().getCards().size();

            if (cardsInHand >= MAX_CARDS_ALLOWED) {
               engine.stand();
               return;
            }

            if (bot.wantsToDouble(botscore) && cardsInHand == 2) { 
                bot.doubleDown();
                engine.hit();
                engine.stand();
            } else if (bot.wantsToHit(botscore)) {
                engine.hit();
            } else {
                engine.stand();
            }

        }

    }

    @Override
    public void playDealerTurns() {

       final Dealer dealer = (Dealer) engine.getCurrentPlayer();
       final int dealerScore = engine.currentScore(dealer.getHand());
       final int cardsInHand = dealer.getHand().getCards().size();

       if (cardsInHand >= MAX_CARDS_ALLOWED) {
           engine.stand();
           return;
       }
       if (dealer.shouldHit(dealerScore)) {
           engine.hit(); 
       } else {
           engine.stand();
       }

    }

    private Player getCurrentHumanPlayer() {
        return Optional.ofNullable(engine.getCurrentPlayer())
        .filter(this::isHumanPlayer)
        .map(p -> (Player) p)
        .orElse(null);
    }
} 
