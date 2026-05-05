package it.unibo.chaosjack.view.impl;

import it.unibo.chaosjack.view.api.MainMenuView;
import javafx.fxml.FXML;
import it.unibo.chaosjack.view.api.MainMenuView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainMenuViewImpl implements MainMenuView{

    @Override
    public void display(Stage stage) {
       VBox layoutPrincipale = new VBox(30);
       layoutPrincipale.setAlignment(Pos.CENTER);
       layoutPrincipale.setStyle("-fx-background-color: #2E8B57;"); // inserisco il colore dello sfondo (in questo csao verde)

       Label titolo = new Label("Choas Jack");
       titolo.setStyle("-fx-text-fill: white;");
       titolo.setFont(Font.font("System", FontWeight.BOLD, 50));

       Button startButton = new Button("Start Botton");
       startButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 30px;");

       startButton.setOnAction(e -> startButtonPressed());
       layoutPrincipale.getChildren().addAll(titolo, startButton);

       Scene scena = new Scene(layoutPrincipale, 800, 600);
        stage.setTitle("Chaos Jack - Menu Principale");
        stage.setScene(scena);
        stage.show();

    }

    private void startButtonPressed() {
        System.out.println("Bottone premuto! Il gioco sta per iniziare...");
        // Qui in futuro metterai il codice per cambiare schermata
    }
    

    
}
