package it.unibo.chaosjack;

import it.unibo.chaosjack.view.api.MainMenuView;
import it.unibo.chaosjack.view.impl.MainMenuViewImpl;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Creiamo l'oggetto del tuo menu
        MainMenuView view = new MainMenuViewImpl();
        
        // 2. Chiamiamo il tuo metodo per far apparire la grafica sulla finestra!
        view.display(primaryStage); 
    }
}
