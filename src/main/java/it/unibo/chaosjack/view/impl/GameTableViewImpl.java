package it.unibo.chaosjack.view.impl;

import java.util.List;
import java.util.function.Consumer;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.view.api.GameTableView;
import it.unibo.chaosjack.view.api.PauseMenuView;
import it.unibo.chaosjack.view.api.PlayerWalletView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Implementation of playing table.
 */
public class GameTableViewImpl implements GameTableView {
    private final StackPane mainRoot;
    private final BorderPane gameTable;
    private final PauseMenuView pauseMenu;
    private final Label statusLabel = new Label("Phase: FIRST BET");
    private final Label potLabel = new Label("Pot: 0 fishes");

    private final Button menuButton = new Button("Menu");
    private final Button pauseButton = new Button("Pause");

    private final Button hitButton = new Button("Hit");
    private final Button standButton = new Button("Stand");
    private final Button doubleButton = new Button("Double Down");

    private final Button bet10Button = new Button("10");
    private final Button bet50Button = new Button("50");
    private final Button bet100Button = new Button("100");

    private final HBox dealerCardsBox = new HBox(-40);
    private final HBox player1CardsBox = new HBox(-40);
    private final HBox player2CardsBox = new HBox(-40);

    private final Label specialRoundLabel = new Label("");
    private final Label player1Title = new Label("");
    private final Label player2Title = new Label("");
    private final Label dealerTitle = new Label("DEALER");

    private final Label player1ScoreLabel = new Label("");
    private final Label player2ScoreLabel = new Label("");
    private final Label dealerScoreLabel = new Label("");

    private final PlayerWalletView player1WalletView = new PlayerWalletViewImpl();
    private final PlayerWalletView player2WalletView = new PlayerWalletViewImpl();


    public GameTableViewImpl() {
        this.mainRoot = new StackPane();
        this.gameTable = new BorderPane();
        this.gameTable.setStyle("-fx-background-color: #2E8B57;");
        this.pauseMenu = new PauseMenuViewImpl();

        menuButton.setStyle("-fx-background-color: #d92811; -fx-text-fill: white; -fx-font-size: 14px;");
        pauseButton.setStyle("-fx-background-color: #ffaa00; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        
        final HBox floattingTopBar = new HBox(10, menuButton, pauseButton);
        floattingTopBar.setPadding(new Insets(10));
        floattingTopBar.setPickOnBounds(false);

        floattingTopBar.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
        this.specialRoundLabel.setPadding(new Insets(15));
        
        this.initLayout();

        this.mainRoot.getChildren().addAll(
            this.gameTable,
            floattingTopBar,
            this.specialRoundLabel,
            this.pauseMenu.getRootNode()
        );

        this.mainRoot.widthProperty().addListener((obs, oldVal, newVal) -> {
            this.gameTable.requestLayout();
        });

        StackPane.setAlignment(floattingTopBar, Pos.TOP_LEFT);
        StackPane.setAlignment(this.specialRoundLabel, Pos.TOP_RIGHT);
    }

    private void initLayout() {
        String scoreStyle= "-fx-text-fill : lightgray; -fx-font-size: 16px; -fx-font-style: italic;";
        dealerScoreLabel.setStyle(scoreStyle);
        player1ScoreLabel.setStyle(scoreStyle);
        player2ScoreLabel.setStyle(scoreStyle);

        dealerCardsBox.setAlignment(Pos.CENTER);
        dealerCardsBox.setMinHeight(150);
        dealerTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        final VBox dealerArea = new VBox(10, dealerTitle, dealerScoreLabel, dealerCardsBox);
        dealerArea.setAlignment(Pos.CENTER);
        dealerArea.setPadding(new Insets(10, 0, 0, 0));

        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        potLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 20px;");
        
        bet10Button.setStyle("-fx-font-size: 14px; -fx-base: #B0CA;");
        bet50Button.setStyle("-fx-font-size: 14px; -fx-base: #8f1150;");
        bet100Button.setStyle("-fx-font-size: 14px; -fx-base: #000000; -fx-text-fill: white;");
        final HBox bettingBox = new HBox(15, bet10Button, bet50Button, bet100Button);
        bettingBox.setAlignment(Pos.BASELINE_CENTER);

        hitButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 20;");
        standButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 20;");
        doubleButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 20;");


        final HBox buttonsBox = new HBox(20, doubleButton, hitButton, standButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(20, 0, 0, 0));

        specialRoundLabel.setStyle("-fx-text-fill: #ffaa00; -fx-font-size: 26px; -fx-font-weight: bold; -fx-effect: dropshodow(gaussian, black, 4, 1, 0, 0);");
        specialRoundLabel.setVisible(false);

        final VBox centerArea = new VBox(20, statusLabel, potLabel, buttonsBox, bettingBox);
        centerArea.setAlignment(Pos.CENTER);    

        player1CardsBox.setAlignment(Pos.CENTER);
        player1CardsBox.setMinHeight(150);

        player2CardsBox.setAlignment(Pos.CENTER);
        player2CardsBox.setMinHeight(150);

        player1Title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        player2Title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        final VBox p1LabelsBox = new VBox(5, player1Title, player1ScoreLabel);
        p1LabelsBox.setAlignment(Pos.CENTER);

        final HBox p1TopBox = new HBox(20, p1LabelsBox, player1WalletView.getRootNode());
        p1TopBox.setAlignment(Pos.CENTER);

        final VBox player1Area = new VBox(10, player1Title, p1TopBox, player1CardsBox);
        player1Area.setAlignment(Pos.CENTER);
        player1Area.setMinWidth(350);

        final VBox p2LabelsBox = new VBox(5, player2Title, player2ScoreLabel);
        p2LabelsBox.setAlignment(Pos.CENTER);

        final HBox p2TopBox = new HBox(20, p2LabelsBox, player2WalletView.getRootNode());
        p2TopBox.setAlignment(Pos.CENTER);

        final VBox player2Area = new VBox(10, player2Title, p2TopBox, player2CardsBox);
        player2Area.setAlignment(Pos.CENTER);
        player2Area.setMinWidth(350);

        final HBox playerContainer = new HBox(50, player1Area, player2Area);
        playerContainer.setAlignment(Pos.CENTER);
        playerContainer.setFillHeight(true);
        
        final ScrollPane scrollPane = new ScrollPane(playerContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setMinHeight(200);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #2E8B57; -fx-border-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        this.gameTable.setTop(dealerArea);
        this.gameTable.setCenter(centerArea);
        
        this.gameTable.setBottom(scrollPane);
        BorderPane.setAlignment(scrollPane, Pos.BOTTOM_CENTER);

        this.setGameState(Table.State.FIRST_BET);
    }
    
    @Override
    public Parent getRootNode() {
        return this.mainRoot;
    }

    @Override
    public void updatePot(int amount) {
       this.potLabel.setText("Pot : " + amount + "fishs");
    }

    @Override
    public void setGameState(Table.State state) {
        Platform.runLater(() -> {
           this.statusLabel.setText("Current phase: " + state.name());
           this.statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        });
    }

    @Override
    public void setPlayerButtons(final boolean disable) {
        Platform.runLater(() -> {
            this.hitButton.setDisable(disable);
            this.standButton.setDisable(disable);
            this.doubleButton.setDisable(disable);
        });
        
    }

    @Override
    public void setBetButton(final boolean disable) {
        Platform.runLater(() -> {
            this.bet10Button.setDisable(disable);
            this.bet50Button.setDisable(disable);
            this.bet100Button.setDisable(disable);
        });
        
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
    public PauseMenuView getPauseMenu() {
        return this.pauseMenu;
    }

    @Override
    public void setPauseHandler(final Runnable handler) {
        this.pauseButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setMenuHandler(final Runnable handler) {
        this.menuButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setSpecialRound(String ruleName) {
        Platform.runLater(() -> {
            boolean isSpecial = ruleName != null && !ruleName.isEmpty();

            this.specialRoundLabel.setVisible(isSpecial);

            if (isSpecial) {
                this.specialRoundLabel.setText("SPECIAL ROUND: " + ruleName.toUpperCase());
            } else {
                this.specialRoundLabel.setText("SPECIAL ROUND: ");
            }
        });
    }

    @Override
    public void setActiveTurn(String activeName) {
        Platform.runLater(() -> {
           String normalStyle = "-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;";
           String activeStyle = "-fx-text-fill: #FFD700; -fx-font-size: 20px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0,8), 10, 0.5, 0, 0);";

            List.of(this.dealerTitle, this.player1Title, this.player2Title)
            .forEach(label -> label.setStyle(
                (activeName != null && activeName.equalsIgnoreCase(label.getText())) ? activeStyle : normalStyle
            ));
        });
    }

    @FXML
    private HBox getDealerCardBox() {
        return this.dealerCardsBox;
    }

    @FXML
    private HBox getPlayer1CardBox() {
        return this.player1CardsBox;
    }

    @FXML
    private HBox getPlayer2CardBox() {
        return this.player2CardsBox;
    }

    @Override
    public void updateDealerCard(final List<Card> cards) {
        Platform.runLater(() -> {
            this.dealerCardsBox.getChildren().clear();
            for (final Card c : cards) {
                this.dealerCardsBox.getChildren().add(new CardViewImpl(c).getRootNode());
            }
        });
    }

    @Override
    public void updatePlayer1Cards(final List<Card> cards) {
        Platform.runLater(() -> {
            this.player1CardsBox.getChildren().clear();
            for (final Card c : cards) {
                this.player1CardsBox.getChildren().add(new CardViewImpl(c).getRootNode());
            }
        });
    }

    @Override
    public void updatePlayer2Cards(final List<Card> cards) {
        Platform.runLater(() -> {
            this.player2CardsBox.getChildren().clear();
            for (final Card c : cards) {
                this.player2CardsBox.getChildren().add(new CardViewImpl(c).getRootNode());
            }
        });
    }

    @Override
    public void setPlayerNames(String name1, String name2) {
       this.player1Title.setText(name1.toUpperCase());
       this.player2Title.setText(name2.toUpperCase());
    }

    @Override
    public void setPlayer1Score(int score) {
        Platform.runLater(() -> 
            this.player1ScoreLabel.setText("Score : " + score)
        );
    }

    @Override
    public void setPlayer2Score(int score) {
        Platform.runLater(() -> 
            this.player2ScoreLabel.setText("Score : " + score)
        );
    }

    @Override
    public void setDealerScore(int score) {
        Platform.runLater(() -> 
            this.dealerScoreLabel.setText("Score : " + score)
        );
    }

    @Override
    public void setPlayer1Wallet(int balance) {
        Platform.runLater(() -> 
           this.player1WalletView.updateBalance(balance)
        );
    }

    @Override
    public void setPlayer2Wallet(int balance) {
        Platform.runLater(() -> 
            this.player2WalletView.updateBalance(balance)
        );
    }

    @Override
    public void resetTable() {
        Platform.runLater(() -> {
            this.dealerCardsBox.getChildren().clear();
            this.player1CardsBox.getChildren().clear();
            this.player2CardsBox.getChildren().clear();

            this.player1ScoreLabel.setText("Score: 0");
            this.player2ScoreLabel.setText("Score: 0");
            this.dealerScoreLabel.setText("Score: 0");

            this.player1WalletView.updateBalance(1000);
            this.player2WalletView.updateBalance(1000);

            this.setActiveTurn(null);
            this.setSpecialRound(null);
        });
    }

    @Override
    public void showResult(String resultMessage) {
        Platform.runLater(() -> {
           this.statusLabel.setText(resultMessage);
           this.statusLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 28px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 3, 1, 0, 0);");
        });
    }
    
}
