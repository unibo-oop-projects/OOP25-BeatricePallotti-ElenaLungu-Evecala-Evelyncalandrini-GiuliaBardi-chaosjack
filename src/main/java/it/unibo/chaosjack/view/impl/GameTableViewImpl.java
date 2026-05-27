package it.unibo.chaosjack.view.impl;

import java.util.function.Consumer;

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

    private final Button menuButton = new Button("Menu");

    private final Button hitButton = new Button("Hit");
    private final Button standButton = new Button("Stand");
    //private final Button betButton = new Button("Bet");
    private final Button doubleButton = new Button("Double Down");

    private final Button bet10Button = new Button("10");
    private final Button bet50Button = new Button("50");
    private final Button bet100Button = new Button("100");

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
        menuButton.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-size: 14px;");
        final HBox topBar = new HBox(menuButton);
        topBar.setAlignment(Pos.TOP_LEFT);
        final VBox topContainer = new VBox(10, topBar, dealerArea);
        this.root.setTop(topContainer);

        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        potLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 20px;");
        
        bet10Button.setStyle("-fx-font-size: 14px; -fx-base: #B0CA;");
        bet50Button.setStyle("-fx-font-size: 14px; -fx-base: #FF69B4;");
        bet100Button.setStyle("-fx-font-size: 14px; -fx-base: #000000; -fx-text-fill: white;");
        final HBox bettingBox = new HBox(15, bet10Button, bet50Button, bet100Button);
        bettingBox.setAlignment(Pos.BASELINE_CENTER);

        hitButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 20;");
        standButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 20;");
        //betButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        doubleButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 20;");


        final HBox buttonsBox = new HBox(20, doubleButton, hitButton, standButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(20, 0, 0, 0));

        final VBox centerArea = new VBox(20, statusLabel, potLabel, buttonsBox, bettingBox);
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
            this.setBetButton(true);
            this.hitButton.setDisable(false);
            this.standButton.setDisable(false);
            this.doubleButton.setDisable(false);
        } else if (state == Table.State.FIRST_BET) {
            this.setBetButton(false);
            this.hitButton.setDisable(true);
            this.standButton.setDisable(true);
            this.doubleButton.setDisable(false);
        } else {
            this.setBetButton(true);
            this.hitButton.setDisable(true);
            this.standButton.setDisable(true);
            this.doubleButton.setDisable(true);
        }
    }

    private void setBetButton(final boolean disable) {
        this.bet10Button.setDisable(disable);
        this.bet50Button.setDisable(disable);
        this.bet100Button.setDisable(disable);
    }

    @Override
    public void setHitHandler(final Runnable handler) {
        this.hitButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setStandHandler(final Runnable handler) {
        this.standButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setBetHandler(final Consumer<Integer> handler) {
        this.bet10Button.setOnAction(e -> handler.accept(10));
        this.bet50Button.setOnAction(e -> handler.accept(50));
        this.bet100Button.setOnAction(e -> handler.accept(100));
    }

    @Override
    public void setDoubleDownHandler(final Runnable handler) {
        this.doubleButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setMenuHandler(final Runnable handler) {
        this.menuButton.setOnAction(e -> handler.run());
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
