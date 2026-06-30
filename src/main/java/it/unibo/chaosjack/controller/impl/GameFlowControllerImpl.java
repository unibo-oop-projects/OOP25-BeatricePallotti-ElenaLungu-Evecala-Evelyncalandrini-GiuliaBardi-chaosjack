package it.unibo.chaosjack.controller.impl;

import java.util.Random;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
import it.unibo.chaosjack.model.impl.DoubleHeartsRule;
import it.unibo.chaosjack.model.impl.RoyalFreezeTurn;
import it.unibo.chaosjack.model.impl.YingYung;
import it.unibo.chaosjack.view.api.MainMenuView;
import it.unibo.chaosjack.view.api.ViewManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * this class implements the GameFlowController interface. Deals with the control of the flow of the game
 */
public final class GameFlowControllerImpl implements GameFlowController {

    private static final int MAX_RANGE = 21;
    private static final int RANDOM = 20;
    private final Random random = new Random();
    private boolean isPaused;
    private PauseTransition currentPause;

    private final GameEngine gameEngine;
    private final ActionController actionController; 
    private final Table table;
    private final MainMenuView mainMenuView;
    private final ViewManager viewManager;

    /**
     * the constructor of GameFlowControllerImpl.
     * 
     * @param gameEngine the game engine that managed the rules of the game.
     * @param actionController the controller with methods for making bets, drawing players.
     * @param mainMenuView the main menu of the game.
     * @param viewManager the view that managing the navigation.
     * @param table the table of the game.
     */

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "the controller must remain sync with external components."
    )

    public GameFlowControllerImpl(final GameEngine gameEngine, 
        final ActionController actionController, final MainMenuView mainMenuView,
         final ViewManager viewManager, 
        final Table table) {
        this.gameEngine = gameEngine;
        this.actionController = actionController;
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

         mainMenuView.setExitHandler(() -> {
            Platform.exit();
        });

        viewManager.getGameTable().setPauseHandler(() -> {
            this.isPaused = true;
            viewManager.getGameTable().getPauseMenu().setVisible(true);
            if (this.currentPause != null) {
                this.currentPause.pause();
            }
        });

        viewManager.getGameTable().getPauseMenu().setResumeHandler(() -> {
            this.isPaused = false;
            viewManager.getGameTable().getPauseMenu().setVisible(false);
            if (this.currentPause != null) {
                this.currentPause.play();
            }
        });

        viewManager.getGameTable().getPauseMenu().setRestartHanlder(() -> {
            this.isPaused = false;
            viewManager.getGameTable().getPauseMenu().setVisible(false);
            if (this.currentPause != null) {
                this.currentPause.stop();
            }
            this.newGame();
        });

        viewManager.getGameTable().getPauseMenu().setExitHandler(() -> {
            viewManager.getGameTable().getPauseMenu().setVisible(false);
            this.viewManager.showMainMenu();
        });

        viewManager.getGameTable().setHitHandler(() -> {
            this.actionController.hit();
            this.phaseOfGame();
        });

        viewManager.getGameTable().setStandHandler(() -> {
            this.actionController.stand();
            this.phaseOfGame();
        });

        viewManager.getGameTable().setBetHandler(amount -> {
            this.actionController.bet(amount);
            this.phaseOfGame();
        });

        viewManager.getGameTable().setDoubleDownHandler(() -> {
            this.actionController.doubleDown();
            this.phaseOfGame();

        });

    }

    @Override
    public void newGame() {
        this.isPaused = false;
        gameEngine.resetGame();
        this.upDateView();
        this.resetScore();
        viewManager.getGameTable().setDealerScore(0);
        this.viewManager.getGameTable().updatePot(0);

        viewManager.getGameTable().setGameState(Table.State.FIRST_BET);

        this.viewManager.getGameTable().setBetButton(false);
        this.viewManager.getGameTable().setPlayerButtons(true);

        for (final Partecipant p : gameEngine.getPlayers()) {
            if (this.humanPlayer(p) && ((Player) p).getWallet() <= 0) {
                    Platform.exit();
            }
        }
        gameEngine.nextTurn(); 

        this.setRound();

    }

    @Override
    public void phaseOfGame() {
        this.upDateView();

        this.viewManager.getGameTable().setActiveTurn(gameEngine.getCurrentPlayer().getName());
        this.viewManager.getGameTable().updatePot(this.table.getPot());

        if (gameEngine.isGameOver()) { 

            final RoundEvaluation evaluation = this.table.getWinner();

            this.viewManager.getGameTable().setGameState(Table.State.RESULTS);
            final String messageToShow;
            final RoundResult.Outcome outcome = evaluation.result().outcome();
            if (evaluation.winners().isEmpty() || outcome == RoundResult.Outcome.DEALER_WON) {
                messageToShow = outcome.getMessage();
            } else {
                final String winnersList = String.join("&", evaluation.winners());
                messageToShow = winnersList + " " + outcome.getMessage();
            }

            this.viewManager.getGameTable().showResult(messageToShow);
        }

        final Table.State state = this.table.getCurrentState();
        final Partecipant p = this.gameEngine.getCurrentPlayer();

        switch (state) {
            case FIRST_BET, FINAL_BET -> {
                this.viewManager.getGameTable().setGameState(state);

                final int singleScore = this.gameEngine.currentScore(p.getHand());

                if (singleScore > MAX_RANGE || this.humanPlayer(p) && ((Player) p).getWallet() <= 0
                    || p instanceof NPC && ((NPC) p).getWallet() <= 0) {
                  this.gameEngine.stand();
                  this.phaseOfGame();
                  break;
                }

                if (this.humanPlayer(p)) {
                  this.viewManager.getGameTable().setBetButton(false);
                  this.viewManager.getGameTable().setPlayerButtons(true);
                  return;
                } else {
                  this.viewManager.getGameTable().setBetButton(true);
                  this.viewManager.getGameTable().setPlayerButtons(true);
                  this.automaticBet();

                }
             break;
            }

            case PLAYING -> {
                viewManager.getGameTable().setGameState(Table.State.PLAYING);

                if (gameEngine.getDealerHand().getCards().isEmpty()) {
                    gameEngine.initialCards();
                    gameEngine.initialCardsDealer();
                    this.upDateView();
                }
                this.automaticShift(); 
                break;
            }

            case DEALER_TURN -> {
                this.viewManager.getGameTable().setBetButton(true);
                this.viewManager.getGameTable().setPlayerButtons(true);

                final boolean anyPlayerToBeat = gameEngine.getPlayers().stream()
                .filter(player -> !(player instanceof Dealer))
                .anyMatch(player -> gameEngine.currentScore(player.getHand()) > 0 
                && gameEngine.currentScore(player.getHand()) <= MAX_RANGE);

                if (!anyPlayerToBeat) {
                    gameEngine.stand();
                    this.phaseOfGame();
                    return;
                }

                gameEngine.dealerTurn();
                this.viewManager.getGameTable().setActiveTurn("dealer");
                this.viewManager.getGameTable().setGameState(state);

                this.automaticShift();
                break;
            }

            default -> {
                break;
            }
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

        if (!this.isPaused) {
         this.currentPause.play();
        }
    }

    @Override 
    public void automaticShift() {

        final Partecipant p1 = this.gameEngine.getCurrentPlayer();
        if (p1.getHand().getCards().isEmpty() && !(p1 instanceof Dealer)) {
            gameEngine.stand();
            this.phaseOfGame();
            return;
        }

        if (this.humanPlayer(p1)) {
            if (gameEngine.currentScore(p1.getHand()) <= MAX_RANGE) {
                viewManager.getGameTable().setPlayerButtons(false);
                viewManager.getGameTable().setBetButton(true);
                return;

            } else {
                gameEngine.stand();
                this.phaseOfGame();

            }
        } 

         this.currentPause = new PauseTransition(Duration.seconds(1));
         this.currentPause.setOnFinished(event -> {

            if (p1 instanceof NPC) {

                viewManager.getGameTable().setPlayerButtons(true);
                viewManager.getGameTable().setBetButton(false);
                actionController.playAutomatedTurns();

            } else if (p1 instanceof Dealer) {

                viewManager.getGameTable().setPlayerButtons(true);
                viewManager.getGameTable().setBetButton(false);
                actionController.playDealerTurns();
            }

            this.phaseOfGame();
         });

        if (!this.isPaused) {
         this.currentPause.play();
        }
    }

    private void setRound() {

        if (random.nextInt(100) < RANDOM) {
            gameEngine.setSpecialRound(this.chooseSpecialRound()); 
        } else {
            gameEngine.setSpecialRound(null);
            this.viewManager.getGameTable().setSpecialRound("");
        }
        this.viewManager.getGameTable().setSpecialRound(gameEngine.getSpecialRound().getDescription() != null 
        ? gameEngine.getSpecialRound().getDescription() : null);
        this.phaseOfGame();

    }

    private SpecialRound chooseSpecialRound() {
        final int choise = random.nextInt(3);
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
            default:
                break;
        }
        return specialRound;
    }

    private void upDateView() {

        viewManager.getGameTable().updateDealerCard(gameEngine.getDealerHand().getCards());
        viewManager.getGameTable().setDealerScore(gameEngine.currentScore(gameEngine.getDealerHand()));

        if (gameEngine.getPlayers().size() >= 2) {
            viewManager.getGameTable().setPlayerNames(
                gameEngine.getPlayers().get(0).getName(),
                gameEngine.getPlayers().get(1).getName()
            );

        }

        for (int i = 0; i < gameEngine.getPlayers().size(); i++) {
            final var p = gameEngine.getPlayers().get(i);
            final int score = gameEngine.currentScore(p.getHand());
            if (i == 0) {
               viewManager.getGameTable().updatePlayer1Cards(gameEngine.getPlayers().get(i).getHand().getCards()); 
               viewManager.getGameTable().setPlayer1Score(score);
               if (p instanceof Player) {
                viewManager.getGameTable().setPlayer1Wallet(((Player) p).getWallet());
               }

            } else if (i == 1) {
                viewManager.getGameTable().updatePlayer2Cards(gameEngine.getPlayers().get(i).getHand().getCards());
                viewManager.getGameTable().setPlayer2Score(score);
                if (p instanceof Player) {
                viewManager.getGameTable().setPlayer2Wallet(((Player) p).getWallet());

               }

            }
        }

    }

    private void resetScore() {
        if (gameEngine.getPlayers().size() >= 2) {
            viewManager.getGameTable().setPlayer1Score(0);
            viewManager.getGameTable().setPlayer2Score(0);
        } else {
            viewManager.getGameTable().setPlayer1Score(0);
        }
    }

    private boolean humanPlayer(final Partecipant p) {
        return p instanceof Player && !(p instanceof NPC);
    }

}
