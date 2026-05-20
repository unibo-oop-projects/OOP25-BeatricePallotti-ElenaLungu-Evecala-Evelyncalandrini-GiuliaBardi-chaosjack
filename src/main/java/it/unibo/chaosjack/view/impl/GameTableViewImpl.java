package it.unibo.chaosjack.view.impl;

import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.view.api.GameTableView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Implementation of playing table.
 */
public class GameTableViewImpl implements GameTableView {
    private final BorderPane root;
    private final Label statusLabel = new Label("Phase: FIRST BET");
    private final Label potLabel = new Label("Pot: 0 fishes");

    private final Button hitButton = new Button("Hit");
    private final Button standButton = new Button("Stand");
    private final Button betButton = new Button("Bet");

    private final HBox dealerCardsBox = new HBox(15);
    private final HBox player1CardsBox = new HBox(15);
    private final HBox player2CardsBox = new HBox(15);

    public GameTableViewImpl() {
        this.root = new BorderPane();
        this.root.setStyle("-fx-background-color: #2E8B57;");
        this.initLayout();
    }

    private void initLayout() {
        dealerCardsBox.setAlignment(Pos.CENTER);
        dealerCardsBox.setMinHeight(150);
        final Label dealerTitle = new Label("DEALER");
        dealerTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        final VBox dealerArea = new VBox(10, dealerTitle, dealerCardsBox);
        dealerArea.setAlignment(Pos.CENTER);
        this.root.setTop(dealerArea);

        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        potLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 20px;");
        
        hitButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        standButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        betButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");

        final HBox buttonsBox = new HBox(20, betButton, hitButton, standButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(20, 0, 0, 0));

        final VBox centerArea = new VBox(20, statusLabel, potLabel, buttonsBox);
        centerArea.setAlignment(Pos.CENTER);
        this.root.setCenter(centerArea);     

        player1CardsBox.setAlignment(Pos.CENTER);
        player1CardsBox.setMinHeight(150);

        player2CardsBox.setAlignment(Pos.CENTER);
        player2CardsBox.setMinHeight(150);

        final Label player1Title = new Label("GIOCATORE 1");
        player1Title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        final Label player2Title = new Label("GIOCATORE 2");
        player2Title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        final VBox player1Area = new VBox(10, player1Title, player1CardsBox);
        player1Area.setAlignment(Pos.CENTER);

        final VBox player2Area = new VBox(10, player2Title, player2CardsBox);
        player2Area.setAlignment(Pos.CENTER);

        final HBox playerContainer = new HBox(100, player1Area, player2Area);
        playerContainer.setAlignment(Pos.CENTER);
        this.root.setBottom(playerContainer);

        this.setGameState(Table.State.FIRST_BET);
    }
    
    @Override
    public Parent getRootNode() {
        return this.root;
    }

    @Override
    public void updatePot(int amount) {
       this.potLabel.setText("Pot : " + amount + "fishs");
    }

    @Override
    public void setGameState(Table.State state) {
        this.statusLabel.setText("Current phase: " + state.name());

        if (state == Table.State.PLAYING) {
            this.betButton.setDisable(true);
            this.hitButton.setDisable(false);
            this.standButton.setDisable(false);
        } else if (state == Table.State.FIRST_BET) {
            this.betButton.setDisable(false);
            this.hitButton.setDisable(true);
            this.standButton.setDisable(true);
        } else {
            this.betButton.setDisable(true);
            this.hitButton.setDisable(true);
            this.standButton.setDisable(true);
        }
    }

    @Override
    public void setHitHandler(Runnable handler) {
        this.hitButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setStandHandler(Runnable handler) {
        this.standButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setBetHandler(Runnable handler) {
        this.betButton.setOnAction(e -> handler.run());
    }

    public HBox getDealerCardBox() {
        return this.dealerCardsBox;
    }

    public HBox getPlayer1CardBox() {
        return this.player1CardsBox;
    }

    public HBox getPlayer2CardBox() {
        return this.player2CardsBox;
    }
    
}
