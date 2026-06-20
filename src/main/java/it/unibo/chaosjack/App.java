package it.unibo.chaosjack;

import java.util.ArrayList;
import java.util.List;

import it.unibo.chaosjack.controller.api.GameFlowController;
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
import it.unibo.chaosjack.model.impl.StandardWallet;
import it.unibo.chaosjack.model.impl.TableImpl;
import it.unibo.chaosjack.model.api.Wallet;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.view.api.GameTableView;
import it.unibo.chaosjack.view.api.MainMenuView;
import it.unibo.chaosjack.view.api.PauseMenuView;
import it.unibo.chaosjack.view.api.ViewManager;
import it.unibo.chaosjack.view.impl.ViewManagerImpl;
import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(final Stage primaryStage) {
        final ViewManager viewManager = new ViewManagerImpl(primaryStage);
        GameTableView gameTableView = viewManager.getGameTable();
        MainMenuView mainMenuView = viewManager.getMainMenu();
        PauseMenuView pauseMenuView = gameTableView.getPauseMenu();

        Deck deck = new StandardDeck();
        Dealer dealer = new DealerImpl();
        
        List<Partecipant> players = new ArrayList<>();
        players.add(new PlayerImpl( "giocatore1", 1000));
        players.add(new NPCimpl( "giocatore2", 1000));

        GameEngineImpl gameEngine = new GameEngineImpl(deck, players, dealer);
        Wallet wallet = new StandardWallet(1000);
        Table table = new TableImpl(List.of("giocatore1", "giocatore2"), gameEngine);
        gameEngine.setTable(table);

        ActionControllerImpl actionController = new ActionControllerImpl(table, gameEngine);
        GameFlowController gameFlow = new GameFlowControllerImpl(gameEngine, actionController, gameTableView, mainMenuView, viewManager, table,pauseMenuView);
        
        viewManager.showMainMenu();
    }

    
}
