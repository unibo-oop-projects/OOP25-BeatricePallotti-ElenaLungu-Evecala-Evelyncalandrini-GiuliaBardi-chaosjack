package it.unibo.chaosjack.view.impl;

import java.util.List;
import java.util.function.Consumer;

import javax.swing.text.FlowView.FlowStrategy;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.view.api.GameTableView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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

    private final FlowPane dealerCardsBox = new FlowPane(17, 17);
    private final FlowPane player1CardsBox = new FlowPane(17, 17);
    private final FlowPane player2CardsBox = new FlowPane(17,17);

    private final Label specialRoundLabel = new Label("");
    private final Label player1Title = new Label("");
    private final Label player2Title = new Label("");
    private final Label dealerTitle = new Label("DEALER");

    public GameTableViewImpl() {
        this.root = new BorderPane();
        this.root.setStyle("-fx-background-color: #2E8B57;");
        this.initLayout();
    }

    private void initLayout() {
        dealerCardsBox.setAlignment(Pos.CENTER);
        dealerCardsBox.setMinHeight(150);
        dealerTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        final VBox dealerArea = new VBox(10, dealerTitle, dealerCardsBox);
        dealerArea.setAlignment(Pos.CENTER);
        menuButton.setStyle("-fx-background-color: #d92811; -fx-text-fill: white; -fx-font-size: 14px;");
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

        specialRoundLabel.setStyle("-fx-text-fill: #FF4500; -fx-font-size: 26px; -fx-font-weight: bold; -fx-effect: dropshodow(gaussian, black, 4, 1, 0, 0);");
        specialRoundLabel.setVisible(false);
        specialRoundLabel.setManaged(false);

        final VBox centerArea = new VBox(20, specialRoundLabel, statusLabel, potLabel, buttonsBox, bettingBox);
        centerArea.setAlignment(Pos.CENTER);
        this.root.setCenter(centerArea);    

        player1CardsBox.setAlignment(Pos.CENTER);
        player1CardsBox.setMinHeight(150);

        player2CardsBox.setAlignment(Pos.CENTER);
        player2CardsBox.setMinHeight(150);

        player1Title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        player2Title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        final VBox player1Area = new VBox(10, player1Title, player1CardsBox);
        player1Area.setAlignment(Pos.CENTER);

        final VBox player2Area = new VBox(10, player2Title, player2CardsBox);
        player2Area.setAlignment(Pos.CENTER);

        /*final HBox playerContainer = new HBox(100, player1Area, player2Area);
        playerContainer.setAlignment(Pos.CENTER);
        this.root.setBottom(playerContainer);*/
        final HBox playerContainer = new HBox(20, player1Area, player2Area);
        playerContainer.setAlignment(Pos.CENTER);

        BorderPane.setMargin(playerContainer, new Insets(0, 0, 60, 0));
        this.root.setBottom(playerContainer);

        //use this for size of card's space
        player1Area.maxWidthProperty().bind(root.widthProperty().divide(2).subtract(20));
        player2Area.maxWidthProperty().bind(root.widthProperty().divide(2).subtract(20));
        
        dealerCardsBox.prefWrapLengthProperty().bind(root.widthProperty().subtract(100));
        player1CardsBox.prefWrapLengthProperty().bind(root.widthProperty().divide(2).subtract(20));
        player2CardsBox.prefWrapLengthProperty().bind(root.widthProperty().divide(2).subtract(20));


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
        Platform.runLater(() -> {
           this.statusLabel.setText("Current phase: " + state.name());

           /*switch (state) {
                case PLAYING -> {
                    this.setBetButton(true);
                    this.setPlayerButtons(!isHumanTurn);
                }
                case FIRST_BET, FINAL_BET -> {
                    this.setBetButton(false);
                    this.setPlayerButtons(isHumanTurn);
                }
                case DEALER_TURN, RESULTS -> {
                    this.setBetButton(true);
                    this.setPlayerButtons(!isHumanTurn);
                }
           }*/
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
    public void setMenuHandler(final Runnable handler) {
        this.menuButton.setOnAction(e -> handler.run());
    }

    @Override
    public void setSpecialRound(String ruleName) {
        Platform.runLater(() -> {
            boolean isSpecial = ruleName != null && !ruleName.isEmpty();

            this.specialRoundLabel.setVisible(isSpecial);
            this.specialRoundLabel.setManaged(isSpecial);

            if (isSpecial) {
                this.specialRoundLabel.setText("SPACIAL ROUND: " + ruleName.toUpperCase());
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
    private FlowPane getDealerCardBox() {
        return this.dealerCardsBox;
    }

    @FXML
    private FlowPane getPlayer1CardBox() {
        return this.player1CardsBox;
    }

    @FXML
    private FlowPane getPlayer2CardBox() {
        return this.player2CardsBox;
    }

    @Override
    public void updateDealerCard(final List<Card> cards) {
        
            this.dealerCardsBox.getChildren().clear();
            for (final Card c : cards) {
                this.dealerCardsBox.getChildren().add(new CardViewImpl(c).getRootNode());
            }
        
    }

    @Override
    public void updatePlayer1Cards(final List<Card> cards) {
        
            this.player1CardsBox.getChildren().clear();
            for (final Card c : cards) {
                this.player1CardsBox.getChildren().add(new CardViewImpl(c).getRootNode());
            }
       
    }

    @Override
    public void updatePlayer2Cards(final List<Card> cards) {
        
            this.player2CardsBox.getChildren().clear();
            for (final Card c : cards) {
                this.player2CardsBox.getChildren().add(new CardViewImpl(c).getRootNode());
            }
       
    }

    @Override
    public void setPlayerNames(String name1, String name2) {
       this.player1Title.setText(name1.toUpperCase());
       this.player2Title.setText(name2.toUpperCase());
    }
    
}
