package it.unibo.chaosjack.view.impl;

import java.util.HashSet;
import java.util.Set;

import it.unibo.chaosjack.model.api.Statistics;
import it.unibo.chaosjack.view.api.StatisticsView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Implementation of the statistics view.
 */
public final class StatisticsViewImpl implements StatisticsView {

    private static final int ROOT_SPACING = 25;
    private static final int ROOT_PADDING = 40;
    private static final int CONTAINER_SPACING = 30;
    private static final int BOX_SPACING = 10;
    private static final int BOX_PADDING = 20;

    @Override
    public Parent createRoot(final Statistics stats, final Runnable onBack) {
       final VBox root = new VBox(ROOT_SPACING);
       root.setAlignment(Pos.CENTER);
       root.setPadding(new Insets(ROOT_PADDING));
       root.setStyle("-fx-background-color: #2b2b2b;");

       final Label titleLabel = new Label("STATISTCS");
       titleLabel.setStyle("-fx-text-fill: #FFD700; -x-font-size: 32px; -fx-font-weight: bold;");

       final Label roundsLabel = new Label("Total round: " + stats.getTotalRounds());
       roundsLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 20px");

       root.getChildren().addAll(titleLabel, roundsLabel);

       final Set<String> players = new HashSet<>();
       players.addAll(stats.getNetProfit().keySet());
       players.addAll(stats.getLossHistory().keySet());

       final HBox playersContainer = new HBox(CONTAINER_SPACING);
       playersContainer.setAlignment(Pos.CENTER);

       for (final String player : players) {
            final VBox playerBox = createPlayerStatsBox(player, stats);
            playersContainer.getChildren().add(playerBox);
       }

       final Button backButton = new Button("Menu");
       backButton.setStyle("-fx-background-color: #FF4444; -fx-text-fill: white; -fx-font-size: 18px;");
       backButton.setOnAction(e -> onBack.run());

       root.getChildren().add(playersContainer);
       root.getChildren().add(backButton);

       return root;
    }

    private VBox createPlayerStatsBox(final String playerName, final Statistics stats) {
        final VBox box = new VBox(BOX_SPACING);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(BOX_PADDING));
        box.setStyle("-fx-background-color: #3b3b3b; -fx-background-radius: 10;");

        final Label nameLabel = new Label(playerName);
        nameLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 24px; -fx-font-weight: bold;");

        final int wins = stats.getWinHistory().getOrDefault(playerName, 0);
        final int blackjacks = stats.getBlackHistory().getOrDefault(playerName, 0);
        final int bonusWins = stats.getBonusHistory().getOrDefault(playerName, 0);
        final int blackBonus = stats.getBlackBonusHistory().getOrDefault(playerName, 0);
        final int losses = stats.getLossHistory().getOrDefault(playerName, 0);
        final int pushes = stats.getPushHistory().getOrDefault(playerName, 0);
        final int netProfit = stats.getNetProfit().getOrDefault(playerName, 0);

        final Label winLabel = createStatLabel("Wins: " + wins);
        final Label blackLabel = createStatLabel("Blackjacks: " + blackjacks);
        final Label bonusWinLabel = createStatLabel("Bonus Wins: " + bonusWins);
        final Label blackBonusLabel = createStatLabel("Blackjack Bonus: " + blackBonus);
        final Label lossLabel = createStatLabel("Losses: " + losses);
        final Label pushLabel = createStatLabel("Pushes: " + pushes);

        final Label profitLabel = new Label("Net Profit: " + netProfit);
        if (netProfit > 0) {
            profitLabel.setStyle("-fx-text-fill: #00FF00; -fx-font-size: 18px; -fx-font-weight: bold;");
        } else if (netProfit < 0) {
            profitLabel.setStyle("-fx-text-fill: #FF4444; -fx-font-size: 18px; -fx-font-weight: bold;");
        } else {
            profitLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 18px; -fx-font-weight: bold;");
        }

        box.getChildren().addAll(
            nameLabel,
            winLabel,
            blackLabel,
            bonusWinLabel,
            blackBonusLabel,
            lossLabel,
            pushLabel,
            profitLabel
        );
        return box;
    }

    private Label createStatLabel(final String text) {
        final Label label = new Label(text);
        label.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16px;");
        return label;
    }

}
