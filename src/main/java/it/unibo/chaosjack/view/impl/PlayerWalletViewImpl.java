package it.unibo.chaosjack.view.impl;

import it.unibo.chaosjack.view.api.PlayerWalletView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Implementation of {@link PlayerWalletView}.
 */
public final class PlayerWalletViewImpl extends VBox implements PlayerWalletView {
    private static final double PREF_WIDTH = 200;
    private static final double PADDING = 14;
    private static final int CORNER_RADIUS = 16;
    private static final double NAME_FONT_SIZE = 14;
    private static final double BALANCE_FONT_SIZE = 22;
    private static final double SPACING = 6;
    private static final double SHADOW_RADIUS = 15;
    private static final double SHADOW_OPACITY = 0.7;
    private static final String GOLD_HEX = "#F5C542";
    private static final String GOLD_BORDER_HEX = "#C9A227";
    private static final String CHIP_EMOJI = "\uD83D\uDCB0";

    private final Label nameLabel;
    private final Label balanceLabel;

    public PlayerWalletViewImpl() {
        super();
        this.setSpacing(SPACING);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(PADDING));
        this.setPrefWidth(PREF_WIDTH);

        this.setStyle(
            "-fx-background-color: rgba(20, 20, 20, 0.85);"
            + " -fx-background-radius: " + CORNER_RADIUS + ";"
            + " -fx-border-color: " + GOLD_BORDER_HEX + ";"
            + " -fx-border-width: 1.5;"
            + " -fx-border-radius: " + CORNER_RADIUS + ";"
        );

        final DropShadow shadow = new DropShadow();
        shadow.setRadius(SHADOW_RADIUS);
        shadow.setColor(Color.color(0, 0, 0, SHADOW_OPACITY));
        shadow.setOffsetY(3);
        this.setEffect(shadow);

        nameLabel = new Label("GIOCATORE");
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, NAME_FONT_SIZE));
        nameLabel.setTextFill(Color.web("#CCCCCC"));
        nameLabel.setStyle("-fx-letter-spacing: 2;");

        balanceLabel = new Label(CHIP_EMOJI + " 0 fiches");
        balanceLabel.setFont(Font.font("System", FontWeight.EXTRA_BOLD, BALANCE_FONT_SIZE));
        balanceLabel.setTextFill(Color.web(GOLD_HEX));

        this.getChildren().addAll(nameLabel, balanceLabel);
    }

    @Override
    public void setPlayerName(final String name) {
        nameLabel.setText(name.toUpperCase(java.util.Locale.ROOT));
    }

    @Override
    public void updateBalance(final int newBalance) {
        balanceLabel.setText(CHIP_EMOJI + " " + newBalance + " fiches");
    }

    @Override
    public String getDisplayedBalance() {
        return balanceLabel.getText();
    }

    @Override
    public String getDisplayedName() {
        return nameLabel.getText();
    }
}
