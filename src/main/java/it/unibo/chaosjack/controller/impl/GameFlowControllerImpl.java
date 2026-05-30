package it.unibo.chaosjack.controller.impl;

import java.util.Random;
import it.unibo.chaosjack.controller.api.ActionController;
import it.unibo.chaosjack.controller.api.GameFlowController;
import it.unibo.chaosjack.model.api.Dealer;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.NPC;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.api.SpecialRound;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.impl.DoubleHeartsRule;
import it.unibo.chaosjack.model.impl.RoyalFreezeTurn;
import it.unibo.chaosjack.model.impl.YingYung;
import it.unibo.chaosjack.view.api.GameTableView;
import it.unibo.chaosjack.view.api.MainMenuView;
import it.unibo.chaosjack.view.api.ViewManager;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class GameFlowControllerImpl implements GameFlowController {

    private GameEngine gameEngine;
    private ActionController actionController; 
    private Table table;
    private GameTableView tableView;
    private MainMenuView mainMenuView;
    private ViewManager viewManager;

    public GameFlowControllerImpl(final GameEngine gameEngine, final ActionController actionController, final GameTableView tableView,
        final MainMenuView mainMenuView, final ViewManager viewManager) {
        this.gameEngine = gameEngine;
        this.actionController = actionController;
        this.tableView = tableView;
        this.mainMenuView = mainMenuView;
        this.viewManager = viewManager;
        this.connectButtons();
    }

    private void connectButtons() {

        mainMenuView.setPlayHandler(() -> {
            viewManager.showGameTable();
            this.newGame();
        });

        tableView.setHitHandler(() -> {
            actionController.hit();
            // *
            this.phaseOfGame();
        });

        tableView.setStandHandler(() -> {
            actionController.stand();
            this.phaseOfGame();
        });

        tableView.setBetHandler(amount -> {
             // per ora metto una puntata fissa, poi la cambierò con quella inserita dal player
            actionController.bet(amount);
            int pot = table.getPot();
            tableView.updatePot(pot);
            
            this.phaseOfGame();
        });

    
    }

    public void newGame() {
        gameEngine.resetGame();
        gameEngine.nextTurn();
        //gameEngine.initialCards();
        tableView.setGameState(Table.State.FIRST_BET);

        Random random = new Random();
        if (random.nextInt(100) < 20) {
            gameEngine.setSpecialRound(this.chooseSpecialRound());
        } else {
            gameEngine.setSpecialRound(null);
        }

        this.phaseOfGame();

    }

    @Override
    public void phaseOfGame() {
        if ( gameEngine.isGameOver()) { // da rivedere
            table.getWinner();
            table.geStatistics();
            tableView.setGameState(Table.State.RESULTS);
        }

        Table.State state = table.getCurrentState();
// NELLE DIVERSE FASI DEL GIOCO NON DEVO AGGIORNARE LA IL SETGAMESTATE?
        switch(state) {
            case FIRST_BET:
            case FINAL_BET:
                tableView.setGameState(state);

                if (gameEngine.getCurrentPlayer() instanceof Player && !(gameEngine.getCurrentPlayer() instanceof NPC)) {
                    return;
                } else {

                    this.automaticBet(); // se è il turno di un npc faccio fare la puntata automatica
                }
                break;
            case PLAYING:
                    int pot = table.getPot();
                    tableView.updatePot(pot);
                    this.automaticShift(); // se è il turno di un npc faccio fare la
                break;
            case DEALER_TURN:
                tableView.setGameState(state);
                if (gameEngine.getCurrentPlayer() instanceof Dealer) {
                    this.automaticShift(); // faccio giocare il dealer
                }
                break;
        }

    }

    @Override
    public void automaticBet() {
        PauseTransition pausa = new PauseTransition(Duration.seconds(1));
        pausa.setOnFinished(event -> {
            if (gameEngine.getCurrentPlayer() instanceof NPC && !(gameEngine.getCurrentPlayer() instanceof Player)) {

                actionController.playAutomatedBet();
            }
            this.phaseOfGame();
        });
        pausa.play();
    }

    @Override // gestisco il timer per far pescare le carte 
    public void automaticShift() {
        if (gameEngine.getCurrentPlayer() instanceof Player && !(gameEngine.getCurrentPlayer() instanceof NPC)) {
            tableView.setGameState(Table.State.PLAYING);
            return;
        } 

            // disattivo i bottoni
         PauseTransition pausa = new PauseTransition(Duration.seconds(1));
         pausa.setOnFinished ( event -> {
            if (gameEngine.getCurrentPlayer() instanceof NPC) {
                actionController.playAutomatedTurns();
            } else if (gameEngine.getCurrentPlayer() instanceof Dealer) {
                actionController.playDealerTurns();
            }

            //String name = this.lastCard(gameEngine.getCurrentPlayer()).getName(); // qui gli dovrei passare il seme e il valore 
            // qui l'evelyn dovrebbe crearmi un metodo in modo tle che poi io lo vada a richiamare per dirgli di aggiornare la view

            this.phaseOfGame();// richiamo questo metodo per far avanzare il turno
            // alla giuli devo dire di cambiare il while con un semplice if 
         } );
        
         pausa.play();
        
    }

    private SpecialRound chooseSpecialRound() {
        int choise = new Random().nextInt(3);
        SpecialRound specialRound = null;
        switch (choise) {
            case 0:
                specialRound = new YingYung();
                break;
            case 1: 
                specialRound = new DoubleHeartsRule();
                break;
            case 2:
                specialRound = new RoyalFreezeTurn();
                break;
        }
        return specialRound;
    }

    /*private Card lastCard(Partecipant p) {
        return p.getHand().getCards().get(p.getHand().getCards().size() - 1);
    }*/

}
