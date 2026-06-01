
package it.unibo.chaosjack.view.impl;

import it.unibo.chaosjack.model.api.Card;
import it.unibo.chaosjack.model.api.CardModifier;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.view.api.CardView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Rappresentazione grafica di una carta da gioco in JavaFX.
 * Implementa l'interfaccia CardView ed estende StackPane.
 */
public final class CardViewImpl extends StackPane implements CardView {

    private static final double WIDTH = 100;
    private static final double HEIGHT = 145;

    public CardViewImpl(final Card card) {
        // Proprietà dimensionali
        this.setPrefSize(WIDTH, HEIGHT);
        this.setMinSize(WIDTH, HEIGHT);
        this.setMaxSize(WIDTH, HEIGHT);

        // Estrazione dati
        final String cardName = card.getName();
        final CardModifier modifier = card.getModifier();

        String suitSymbol = "?";
        String suitColor = "black";
        boolean isRed = false;

        if (cardName.contains("HEARTS")) {
            suitSymbol = "♥";
            suitColor = "#d32f2f"; // Rosso classico
            isRed = true;
        } else if (cardName.contains("DIAMONDS")) {
            suitSymbol = "♦";
            suitColor = "#d32f2f";
            isRed = true;
        } else if (cardName.contains("CLUBS")) {
            suitSymbol = "♣";
            suitColor = "#212121";
        } else if (cardName.contains("SPADES")) {
            suitSymbol = "♠";
            suitColor = "#212121";
        }

        String rankSymbol = "?";
        for (final Rank r : Rank.values()) {
            if (cardName.contains(r.name())) {
                rankSymbol = getRankSymbol(r);
                break;
            }
        }

        // 1. Sfondo Solido o Semi-trasparente (Rectangle)
        final Rectangle background = new Rectangle(WIDTH, HEIGHT);
        background.setArcWidth(12);
        background.setArcHeight(12);

        javafx.scene.paint.Paint bgPaint = Color.WHITE;
        Color strokeColor = Color.web("#cccccc");
        double strokeWidth = 1.2;
        String textFillColor = suitColor;

        switch (modifier) {
            case BUST_MAGNET:
                bgPaint = Color.WHITE; // Sfondo bianco uguale alle altre carte
                strokeColor = Color.web("#8e0000"); // Bordo rosso scuro
                strokeWidth = 4.0; // Bordo ancora più spesso rispetto alle altre (4.0 contro 2.0/1.2)
                textFillColor = suitColor; // Colore del seme (rosso o nero)
                break;
            case REVERSE:
                bgPaint = Color.WHITE; // Sfondo bianco uguale alle altre
                strokeColor = Color.web("#1565c0"); // Bordo blu scuro elegante
                strokeWidth = 3.0; // Bordo spesso (3.0 per REVERSE, 4.0 per BUST, 1.2 per standard)
                textFillColor = suitColor; // Colore normale del seme su sfondo chiaro
                break;
            case GHOST:
                // Effetto Fumo: Sfondo semi-trasparente bianco/grigio (45% opacità)
                bgPaint = Color.color(0.96, 0.96, 0.96, 0.45);
                strokeColor = Color.web("#9e9e9e"); // Bordo grigio
                strokeWidth = 2.0;
                // Testo del colore del seme ma semitrasparente (più opaco dello sfondo: 75% opacità)
                textFillColor = isRed ? "rgba(211, 47, 47, 0.75)" : "rgba(33, 33, 33, 0.75)";
                break;
            case NONE:
            default:
                bgPaint = Color.WHITE;
                strokeColor = Color.web("#cccccc");
                strokeWidth = 1.2;
                textFillColor = suitColor;
                break;
        }

        background.setFill(bgPaint);
        background.setStroke(strokeColor);
        background.setStrokeWidth(strokeWidth);
        this.getChildren().add(background);

        // Ombreggiatura (rimossa per la GHOST per dare un effetto spettrale più realistico, mantenuta per le altre)
        if (modifier != CardModifier.GHOST) {
            final DropShadow shadow = new DropShadow();
            shadow.setColor(Color.color(0, 0, 0, 0.15));
            shadow.setRadius(6);
            shadow.setOffsetX(2);
            shadow.setOffsetY(3);
            this.setEffect(shadow);
        }

        // 2. Costruzione del layout del contenuto
        final BorderPane cardContent = new BorderPane();
        cardContent.setPadding(new Insets(6));

        // Angolo in alto a sinistra: Rango e Seme
        final Label topLeftLabel = new Label(rankSymbol + "\n" + suitSymbol);
        topLeftLabel.setStyle("-fx-text-fill: " + textFillColor + "; -fx-font-size: 13px; -fx-font-weight: bold; -fx-line-spacing: -2;");
        BorderPane.setAlignment(topLeftLabel, Pos.TOP_LEFT);
        cardContent.setTop(topLeftLabel);

        // Centro: Seme grande per le carte normali, calamita vettoriale per Bust Magnet, o frecce vettoriali per Reverse
        if (modifier == CardModifier.BUST_MAGNET) {
            final StackPane magnetPane = new StackPane();
            magnetPane.setMaxSize(40, 50);

            final javafx.scene.shape.SVGPath leftHalf = new javafx.scene.shape.SVGPath();
            leftHalf.setContent("M 10 10 H 16 V 32 A 4 4 0 0 0 20 36 V 42 A 10 10 0 0 1 10 32 Z");
            leftHalf.setFill(Color.web("#8e0000")); // Polo Nord - Rosso scuro in linea con il bordo

            final javafx.scene.shape.SVGPath rightHalf = new javafx.scene.shape.SVGPath();
            rightHalf.setContent("M 30 10 H 24 V 32 A 4 4 0 0 1 20 36 V 42 A 10 10 0 0 0 30 32 Z");
            rightHalf.setFill(Color.web("#757575")); // Polo Sud - Grigio metallo

            final javafx.scene.Group magnetGroup = new javafx.scene.Group(leftHalf, rightHalf);
            magnetGroup.setScaleX(1.4);
            magnetGroup.setScaleY(1.4);

            magnetPane.getChildren().add(magnetGroup);
            cardContent.setCenter(magnetPane);
        } else if (modifier == CardModifier.REVERSE) {
            final StackPane reversePane = new StackPane();
            reversePane.setMaxSize(40, 50);

            final javafx.scene.shape.SVGPath arrows = new javafx.scene.shape.SVGPath();
            arrows.setContent("M 20 10 C 25 10, 35 15, 35 25 H 39 L 34 32 L 29 25 H 33 C 33 18, 25 14, 20 14 Z M 20 40 C 15 40, 5 35, 5 25 H 1 L 6 18 L 11 25 H 7 C 7 32, 15 36, 20 36 Z");
            arrows.setFill(Color.web("#1565c0")); // Frecce blu in linea con il bordo della carta

            final javafx.scene.Group arrowsGroup = new javafx.scene.Group(arrows);
            arrowsGroup.setScaleX(1.3);
            arrowsGroup.setScaleY(1.3);

            reversePane.getChildren().add(arrowsGroup);
            cardContent.setCenter(reversePane);
        } else {
            final Label centerLabel = new Label(suitSymbol);
            centerLabel.setStyle("-fx-text-fill: " + textFillColor + "; -fx-font-size: 44px;");
            cardContent.setCenter(centerLabel);
        }

        // Angolo in basso a destra (ruotato di 180 gradi)
        final Label bottomRightLabel = new Label(rankSymbol + "\n" + suitSymbol);
        bottomRightLabel.setStyle("-fx-text-fill: " + textFillColor + "; -fx-font-size: 13px; -fx-font-weight: bold; -fx-line-spacing: -2;");
        bottomRightLabel.setRotate(180);
        BorderPane.setAlignment(bottomRightLabel, Pos.BOTTOM_RIGHT);
        cardContent.setBottom(bottomRightLabel);

        this.getChildren().add(cardContent);

        // 3. Badge identificativo del modificatore in alto a destra
        if (modifier != CardModifier.NONE) {
            final HBox topBadges = new HBox(4);
            topBadges.setAlignment(Pos.TOP_RIGHT);
            topBadges.setPadding(new Insets(4, 4, 0, 0));

            final Label modBadge = new Label();
            String modStyle = "";
            switch (modifier) {
                case BUST_MAGNET:
                    modBadge.setText("BUST");
                    modStyle = "-fx-background-color: #b71c1c; -fx-text-fill: white; -fx-font-size: 8px; -fx-font-weight: bold; -fx-padding: 1 4; -fx-background-radius: 3;";
                    break;
                case REVERSE:
                    modBadge.setText("REV");
                    modStyle = "-fx-background-color: #1565c0; -fx-text-fill: white; -fx-font-size: 8px; -fx-font-weight: bold; -fx-padding: 1 4; -fx-background-radius: 3;";
                    break;
                case GHOST:
                    modBadge.setText("GHOST");
                    // Badge per Ghost anch'esso leggermente semitrasparente in linea con l'effetto fumo
                    modStyle = "-fx-background-color: rgba(66, 66, 66, 0.75); -fx-text-fill: white; -fx-font-size: 8px; -fx-font-weight: bold; -fx-padding: 1 4; -fx-background-radius: 3;";
                    break;
                default:
                    break;
            }
            modBadge.setStyle(modStyle);
            topBadges.getChildren().add(modBadge);

            StackPane.setAlignment(topBadges, Pos.TOP_RIGHT);
            this.getChildren().add(topBadges);
        }
    }

    private String getRankSymbol(final Rank rank) {
        switch (rank) {
            case ACE: return "A";
            case TWO: return "2";
            case THREE: return "3";
            case FOUR: return "4";
            case FIVE: return "5";
            case SIX: return "6";
            case SEVEN: return "7";
            case EIGHT: return "8";
            case NINE: return "9";
            case TEN: return "10";
            case JACK: return "J";
            case QUEEN: return "Q";
            case KING: return "K";
            default: return "?";
        }
    }

    @Override
    public Parent getRootNode() {
        return this;
    }
}
