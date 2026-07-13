package com.univalle._0zo.controller;

import com.univalle._0zo.model.Card;
import com.univalle._0zo.model.Mazo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la mesa del juego.
 * Maneja la visualización de los jugadores máquina y la mesa.
 */
public class TableController {

    @FXML
    private Label machine1Label;
    @FXML
    private Label machine2Label;
    @FXML
    private Label machine3Label;
    @FXML
    private Label tableCardLabel;
    @FXML
    private Label tableSumLabel;
    @FXML
    private HBox playerHandBox;

    private Mazo mazo;
    private List<Card> manoCards;
    private int tableSum;

    public void setMachines(int numMachines) {
        machine2Label.setVisible(numMachines >= 2);
        machine2Label.setManaged(numMachines >= 2);
        machine3Label.setVisible(numMachines >= 3);
        machine3Label.setManaged(numMachines >= 3);

        dealInitialCards();
    }

    private void dealInitialCards() {
        mazo = new Mazo();
        manoCards = new ArrayList<>();
        tableSum = 0;

        // Repartir 4 cartas al jugador
        for (int i = 0; i < 4; i++) {
            manoCards.add(mazo.drawCard());
        }

        // Carta inicial en la mesa
        Card tableCard = mazo.drawCard();
        tableSum = tableCard.getGameValue();

        // Mostrar carta de la mesa
        tableCardLabel.setText("Carta: " + tableCard.toString());
        tableSumLabel.setText("Suma: " + tableSum);

        // Mostrar mano del jugador
        showPlayerHand();
    }

    private void showPlayerHand() {
        playerHandBox.getChildren().clear();
        for (Card card : manoCards) {
            Label cardLabel = new Label(card.toString());
            cardLabel.setStyle("-fx-font-size: 18px; -fx-padding: 8 12; -fx-border-color: black; -fx-background-color: white;");
            playerHandBox.getChildren().add(cardLabel);
        }
    }
}
