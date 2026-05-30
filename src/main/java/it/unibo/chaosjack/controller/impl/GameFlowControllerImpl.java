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
        final MainMenuView mainMenuView, final ViewManager viewManager, final Table table) {
        this.gameEngine = gameEngine;
        this.actionController = actionController;
        this.tableView = tableView;
        this.mainMenuView = mainMenuView;
        this.viewManager = viewManager;
        this.table = table;

        this.connectButtons();
        
    }

    private void connectButtons() {

        mainMenuView.setPlayHandler(() -> {
            this.viewManager.showGameTable();
            this.newGame();
        });

        tableView.setHitHandler(() -> {
            this.actionController.hit();
            
            this.phaseOfGame();
        });

        tableView.setStandHandler(() -> {
            this.actionController.stand();
            
            this.phaseOfGame();
        });

        tableView.setBetHandler(amount -> {
             // per ora metto una puntata fissa, poi la cambierò con quella inserita dal player
            this.actionController.bet(amount);
            
            this.phaseOfGame();
        });

    
    }

    public void newGame() {
        System.err.println("new game");
        gameEngine.resetGame();
        gameEngine.nextTurn(); // faccio partire il gioco e faccio avanzare il turno in modo che arrivi al primo giocatore
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
        this.upDateView();
        
        this.tableView.updatePot(this.table.getPot());
        

        if ( gameEngine.isGameOver()) { // da rivedere
            this.table.getWinner();
            
            this.tableView.setGameState(Table.State.RESULTS);
        }

        Table.State state = this.table.getCurrentState();
// NELLE DIVERSE FASI DEL GIOCO NON DEVO AGGIORNARE LA IL SETGAMESTATE?
        switch(state) {
            case FIRST_BET:
            case FINAL_BET:
                this.tableView.setGameState(state);
                if (gameEngine.getCurrentPlayer() instanceof Player && !(gameEngine.getCurrentPlayer() instanceof NPC)) {
                    return;
                } else {

                    this.automaticBet(); // se è il turno di un npc faccio fare la puntata automatica
                }
                break;
            case PLAYING:
                if (gameEngine.getDealerHand().getCards().isEmpty()) {
                    gameEngine.initialCards();
                    this.upDateView();
                }
                this.automaticShift(); // se è il turno di un npc faccio fare la
                break;
            case DEALER_TURN:
                this.tableView.setGameState(state);
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
            if (gameEngine.getCurrentPlayer() instanceof NPC) {
                
                actionController.playAutomatedBet();
                this.phaseOfGame();
            }
            
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

            // richiamo questo metodo per far avanzare il turno
            // alla giuli devo dire di cambiare il while con un semplice if 
            this.phaseOfGame();
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

    private void upDateView() {
        if (gameEngine.getPlayers().size() >= 2) {
            tableView.setPlayerNames(
                gameEngine.getPlayers().get(0).getName(),
                gameEngine.getPlayers().get(1).getName()
            );
            
        }

        tableView.updateDealerCard(gameEngine.getDealerHand().getCards());

        if ( gameEngine.getPlayers().size() >= 1) {
            tableView.updatePlayer1Cards(gameEngine.getPlayers().get(0).getHand().getCards());
        } 

        if (gameEngine.getPlayers().size() >= 2) {
            tableView.updatePlayer2Cards(gameEngine.getPlayers().get(1).getHand().getCards());
        }


    }

    

    
    /*private Card lastCard(Partecipant p) {
        return p.getHand().getCards().get(p.getHand().getCards().size() - 1);
    }*/

}
