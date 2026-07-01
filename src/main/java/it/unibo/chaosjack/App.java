package it.unibo.chaosjack;

import java.util.ArrayList;
import java.util.List;

import it.unibo.chaosjack.controller.impl.ActionControllerImpl;
import it.unibo.chaosjack.controller.impl.GameFlowControllerImpl;
import it.unibo.chaosjack.model.api.Dealer;
import it.unibo.chaosjack.model.api.Deck;
import it.unibo.chaosjack.model.api.Partecipant;
import it.unibo.chaosjack.model.impl.DealerImpl;
import it.unibo.chaosjack.model.impl.GameEngineImpl;
import it.unibo.chaosjack.model.impl.NPCimpl;
import it.unibo.chaosjack.model.impl.PlayerImpl;
import it.unibo.chaosjack.model.impl.StandardDeck;
import it.unibo.chaosjack.model.impl.TableImpl;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.view.api.MainMenuView;
import it.unibo.chaosjack.view.api.ViewManager;
import it.unibo.chaosjack.view.impl.ViewManagerImpl;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class for ChaosJack.
 */
public class App extends Application {

    /**
     * Initializes the core game components, links the Model, View, and Controller.
     * 
     * @param primaryStage the primary stage for this application.
     */
    @Override
    public void start(final Stage primaryStage) {
        final ViewManager viewManager = new ViewManagerImpl(primaryStage);
        final MainMenuView mainMenuView = viewManager.getMainMenu();

        final Deck deck = new StandardDeck();
        final Dealer dealer = new DealerImpl();

        final List<Partecipant> players = new ArrayList<>();
        final int initialBalance = 1000;
        players.add(new PlayerImpl("Player", initialBalance));
        players.add(new NPCimpl("NPC", initialBalance));

        final GameEngineImpl gameEngine = new GameEngineImpl(deck, players, dealer);
        final Table table = new TableImpl(List.of("Player", "NPC"), gameEngine);
        gameEngine.setTable(table);

        final ActionControllerImpl actionController = new ActionControllerImpl(table, gameEngine);
        new GameFlowControllerImpl(
            gameEngine, actionController, mainMenuView, viewManager, table
        );

        viewManager.showMainMenu();
    }
}
