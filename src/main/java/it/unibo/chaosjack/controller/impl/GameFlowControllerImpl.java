package it.unibo.chaosjack.controller.impl;

import java.util.Random;
import it.unibo.chaosjack.controller.api.ActionController;
import it.unibo.chaosjack.controller.api.GameFlowController;
import it.unibo.chaosjack.model.api.Dealer;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.NPC;
import it.unibo.chaosjack.model.api.Partecipant;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.api.RoundEvaluation;
import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.api.SpecialRound;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.Table.State;
import it.unibo.chaosjack.model.impl.DoubleHeartsRule;
import it.unibo.chaosjack.model.impl.RoyalFreezeTurn;
import it.unibo.chaosjack.model.impl.YingYung;
import it.unibo.chaosjack.view.api.GameTableView;
import it.unibo.chaosjack.view.api.MainMenuView;
import it.unibo.chaosjack.view.api.ViewManager;
import it.unibo.chaosjack.view.api.PauseMenuView;
import javafx.animation.PauseTransition;
import javafx.util.Duration;


public class GameFlowControllerImpl implements GameFlowController {

    private GameEngine gameEngine;
    private ActionController actionController; 
    private Table table;
    private GameTableView tableView;
    private MainMenuView mainMenuView;
    private ViewManager viewManager;
    private PauseTransition currentPause;
    private boolean isPaused = false;

    

    public GameFlowControllerImpl(final GameEngine gameEngine, final ActionController actionController, final GameTableView tableView,
        final MainMenuView mainMenuView, final ViewManager viewManager, final Table table, final PauseMenuView pause) {
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

        mainMenuView.setStatsHandler(() -> {

            this.viewManager.showStatistics(this.table.getStatistics());

        });

         mainMenuView.setExitHandler( () -> {
            System.exit(0);
        });

        tableView.setPauseHandler(() -> {
            this.isPaused = true;
            tableView.getPauseMenu().setVisible(true);
            if(this.currentPause != null){
                this.currentPause.pause();
            }
        });

        tableView.getPauseMenu().setResumeHandler(() -> {
            this.isPaused = false;
            tableView.getPauseMenu().setVisible(false);
            if(this.currentPause != null){
                this.currentPause.play();
            }
        });

        tableView.getPauseMenu().setRestartHanlder(() -> {
            this.isPaused = false;
            tableView.getPauseMenu().setVisible(false);
            if(this.currentPause != null){
                this.currentPause.stop();
            }
            this.newGame();
        });

        tableView.getPauseMenu().setExitHandler(() -> {
            tableView.getPauseMenu().setVisible(false);
            this.viewManager.showMainMenu();
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
            this.actionController.bet(amount);
            this.phaseOfGame();
        });

        tableView.setDoubleDownHandler(() -> {
            this.actionController.doubleDown();
            this.phaseOfGame();

        });

    }

    public void newGame() {
        this.isPaused = false;
        gameEngine.resetGame();
        this.upDateView();
        this.reset_score();
        tableView.setDealerScore(0);
        this.tableView.updatePot(0);

        tableView.setGameState(Table.State.FIRST_BET);

        this.tableView.setBetButton(false);
        this.tableView.setPlayerButtons(true);
        gameEngine.nextTurn(); 

        this.setRound();

    }


    @Override
    public void phaseOfGame() {
        this.upDateView();

        this.tableView.setActiveTurn(gameEngine.getCurrentPlayer().getName());
        this.tableView.updatePot(this.table.getPot());

        if ( gameEngine.isGameOver()) { 
            
            RoundEvaluation evaluation = this.table.getWinner();
            
            
            this.tableView.setGameState(Table.State.RESULTS);
            String messageToShow;
            RoundResult.Outcome outcome = evaluation.result().outcome();
            if (evaluation.winners().isEmpty() || outcome == RoundResult.Outcome.DEALER_WON) {
                messageToShow = outcome.getMessage();
            } else {
                String winnersList = String.join("&",evaluation.winners());
                messageToShow = winnersList.toUpperCase()+""+outcome.getMessage();
            }
            
            this.tableView.showResult(messageToShow);
        }

        Table.State state = this.table.getCurrentState();

        switch(state) {
            case FIRST_BET:
            case FINAL_BET:
             this.tableView.setGameState(state);
               
                if (this.humanPlayer(gameEngine.getCurrentPlayer()) && (table.getCurrentState() == State.FINAL_BET) ) {
                    if (gameEngine.currentScore(gameEngine.getCurrentPlayer().getHand()) > 21) { 
                        this.gameEngine.stand();
                        this.phaseOfGame();
                    } else {
                    this.tableView.setBetButton(false);
                    this.tableView.setPlayerButtons(true);
                    return;
                    }

                } else {

                    if (gameEngine.currentScore(gameEngine.getCurrentPlayer().getHand()) > 21) {
                        gameEngine.stand();
                        this.phaseOfGame();
                    } else {
                    this.tableView.setBetButton(false); 
                    this.tableView.setPlayerButtons(true);
                    this.automaticBet(); 
                    }
                }
                break;

            case PLAYING:
                tableView.setGameState(Table.State.PLAYING);
                
                if (gameEngine.getDealerHand().getCards().isEmpty()) {
                    gameEngine.initialCards();
                    this.upDateView();
                }
                this.automaticShift(); 
                break;

            case DEALER_TURN:
                this.tableView.setBetButton(true);
                this.tableView.setPlayerButtons(true);

                gameEngine.dealerTurn();
                this.tableView.setActiveTurn("dealer");
                this.tableView.setGameState(state);

                this.automaticShift(); // faccio giocare il dealer
                break;
        }

    }

    @Override
    public void automaticBet() {

        this.currentPause = new PauseTransition(Duration.seconds(1));
        this.currentPause.setOnFinished(event -> {
            if (gameEngine.getCurrentPlayer() instanceof NPC) {
                
                actionController.playAutomatedBet();
                this.phaseOfGame();
            }
            
        });

        if(!this.isPaused){
         this.currentPause.play();
        }
    }

    @Override 
    public void automaticShift() {

        if (this.humanPlayer(this.gameEngine.getCurrentPlayer())) {
            if (gameEngine.currentScore(gameEngine.getCurrentPlayer().getHand())<=21) {
                tableView.setPlayerButtons(false);
                tableView.setBetButton(true);
                return;
            
            } else {
                gameEngine.stand();
                this.phaseOfGame();
                
            }
            
        } 

         this.currentPause = new PauseTransition(Duration.seconds(1));
         this.currentPause.setOnFinished ( event -> {

            if (gameEngine.getCurrentPlayer() instanceof NPC) {

                tableView.setPlayerButtons(true);
                tableView.setBetButton(false);
                actionController.playAutomatedTurns();
                
            } else if (gameEngine.getCurrentPlayer() instanceof Dealer) {

                tableView.setPlayerButtons(true);
                tableView.setBetButton(false);
                actionController.playDealerTurns();
            }

            this.phaseOfGame();
         } );

        if(!this.isPaused){
         this.currentPause.play();
        }
    }

    private void setRound() {
        Random random = new Random();
        if (random.nextInt(100) < 20) {
            gameEngine.setSpecialRound(this.chooseSpecialRound()); 
        } else {
            gameEngine.setSpecialRound(null);
            this.tableView.setSpecialRound("");
        }
        this.tableView.setSpecialRound(gameEngine.getSpecialRound().getDescription() != null ? gameEngine.getSpecialRound().getDescription() : null);// aggiorno la view in modo da mostrare il round speciale se è presente
        this.phaseOfGame();

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

        tableView.updateDealerCard(gameEngine.getDealerHand().getCards());
        tableView.setDealerScore(gameEngine.currentScore(gameEngine.getDealerHand()));

        if (gameEngine.getPlayers().size() >= 2) {
            tableView.setPlayerNames(
                gameEngine.getPlayers().get(0).getName(),
                gameEngine.getPlayers().get(1).getName()
            );
            
        }

        for ( int i = 0; i < gameEngine.getPlayers().size(); i++ ) {
            final var p = gameEngine.getPlayers().get(i);
            int score = gameEngine.currentScore(p.getHand());
            if (i==0) {
               tableView.updatePlayer1Cards(gameEngine.getPlayers().get(i).getHand().getCards()); 
               tableView.setPlayer1Score(score);
               if (p instanceof Player) {
                tableView.setPlayer1Wallet(((Player)p).getWallet());
               }
               
            } else if (i==1) {
                tableView.updatePlayer2Cards(gameEngine.getPlayers().get(i).getHand().getCards());
                tableView.setPlayer2Score(score);
                if (p instanceof Player) {
                tableView.setPlayer2Wallet(((Player)p).getWallet());

               }
                
            }
        }

    }

    private void reset_score() {
        if (gameEngine.getPlayers().size() >= 2) {
            tableView.setPlayer1Score(0);
            tableView.setPlayer2Score(0);
        } else {
            tableView.setPlayer1Score(0);
        }
    }

    private boolean humanPlayer(Partecipant p) {
        return p instanceof Player && !(p instanceof NPC);
    }

}
