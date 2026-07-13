package com.univalle._0zo.controller;

import com.univalle._0zo.model.Card;
import com.univalle._0zo.model.Mazo;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la mesa del juego.
 * Maneja turnos, jugadas del humano y de las máquinas.
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
    private Label statusLabel;
    @FXML
    private HBox playerHandBox;

    private Mazo mazo;
    private int tableSum;
    private Card lastTableCard;
    private int numMaquinas;

    // Mano del jugador humano
    private List<Card> manoHumano;
    private boolean humanoVivo;

    // Manos de las máquinas
    private List<List<Card>> manosMaquinas;
    private List<Boolean> maquinasVivas;

    // Control de turnos
    private boolean turnoHumano;
    private int turnoMaquinaActual; // índice de la máquina que le toca jugar

    public void setMachines(int numMachines) {
        this.numMaquinas = numMachines;
        machine1Label.setVisible(true);
        machine1Label.setManaged(true);
        machine2Label.setVisible(numMachines >= 2);
        machine2Label.setManaged(numMachines >= 2);
        machine3Label.setVisible(numMachines >= 3);
        machine3Label.setManaged(numMachines >= 3);

        dealInitialCards();
    }

    private void dealInitialCards() {
        mazo = new Mazo();
        tableSum = 0;
        turnoHumano = true;
        humanoVivo = true;
        turnoMaquinaActual = 0;

        // Repartir 4 cartas al humano
        manoHumano = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            manoHumano.add(mazo.drawCard());
        }

        // Repartir 4 cartas a cada máquina
        manosMaquinas = new ArrayList<>();
        maquinasVivas = new ArrayList<>();
        for (int m = 0; m < numMaquinas; m++) {
            List<Card> mano = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                mano.add(mazo.drawCard());
            }
            manosMaquinas.add(mano);
            maquinasVivas.add(true);
        }

        // Carta inicial en la mesa
        Card tableCard = mazo.drawCard();
        tableSum = tableCard.getGameValue();
        lastTableCard = tableCard;

        tableCardLabel.setText("Carta: " + tableCard.toString());
        tableSumLabel.setText("Suma: " + tableSum);
        statusLabel.setText("Tu turno");

        showPlayerHand();
    }

    private void showPlayerHand() {
        playerHandBox.getChildren().clear();
        for (int i = 0; i < manoHumano.size(); i++) {
            Card card = manoHumano.get(i);
            Label cardLabel = new Label(card.toString());
            cardLabel.setStyle("-fx-font-size: 18px; -fx-padding: 8 12; -fx-border-color: black; -fx-background-color: white; -fx-cursor: hand;");

            if (turnoHumano) {
                int index = i;
                cardLabel.setOnMouseClicked(e -> onPlayCard(index));
            } else {
                cardLabel.setStyle("-fx-font-size: 18px; -fx-padding: 8 12; -fx-border-color: gray; -fx-background-color: #ddd;");
            }

            playerHandBox.getChildren().add(cardLabel);
        }
    }

    private void onPlayCard(int index) {
        if (!turnoHumano) return;

        Card card = manoHumano.get(index);
        int value;

        // Si es As, preguntar si vale 1 o 10
        if (card.isAce()) {
            value = preguntarValorAs();
        } else {
            value = card.getGameValue();
        }

        int newSum = tableSum + value;

        if (newSum > 50) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Jugada no valida");
            alert.setHeaderText(null);
            alert.setContentText("No puedes jugar " + card.toString() + " porque la suma seria " + newSum + " y excede 50.");
            alert.showAndWait();
            return;
        }

        // Jugar la carta
        manoHumano.remove(index);
        tableSum = newSum;
        lastTableCard = card;

        tableCardLabel.setText("Carta: " + card.toString());
        tableSumLabel.setText("Suma: " + tableSum);

        // Robar carta del mazo
        if (!mazo.isEmpty()) {
            manoHumano.add(mazo.drawCard());
        }

        // Verificar si el humano puede jugar alguna carta
        if (!puedeJugar(manoHumano)) {
            humanoVivo = false;
            statusLabel.setText("Has sido eliminado! No puedes jugar ninguna carta.");
            // Enviar cartas del humano al fondo del mazo
            mazo.addCardsToBottom(manoHumano);
            manoHumano.clear();
            showPlayerHand();

            if (hayGanador()) {
                mostrarGanador();
                return;
            }
            // Saltar al siguiente turno máquina
            turnoHumano = false;
            turnoMaquinaActual = buscarSiguienteMaquinaViva(0);
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(e -> jugarMaquina());
            delay.play();
            return;
        }

        showPlayerHand();

        // Turno de las máquinas
        turnoHumano = false;
        showPlayerHand();

        turnoMaquinaActual = buscarSiguienteMaquinaViva(0);
        PauseTransition delay = new PauseTransition(Duration.seconds(2 + Math.random() * 2));
        delay.setOnFinished(e -> jugarMaquina());
        delay.play();
    }

    private void jugarMaquina() {
        int idx = turnoMaquinaActual;

        // Verificar que esta máquina sigue viva
        if (!maquinasVivas.get(idx)) {
            siguienteMaquina();
            return;
        }

        List<Card> mano = manosMaquinas.get(idx);

        // La máquina juega la primera carta válida que encuentre
        int cartaJugada = -1;
        int valorUsado = 0;
        for (int i = 0; i < mano.size(); i++) {
            Card c = mano.get(i);
            int val;
            // Si es As, intenta con 10 primero, si no con 1
            if (c.isAce()) {
                if (tableSum + 10 <= 50) {
                    val = 10;
                } else {
                    val = 1;
                }
            } else {
                val = c.getGameValue();
            }
            int nuevaSuma = tableSum + val;
            if (nuevaSuma <= 50) {
                cartaJugada = i;
                valorUsado = val;
                break;
            }
        }

        if (cartaJugada == -1) {
            // Máquina eliminada
            maquinasVivas.set(idx, false);
            mazo.addCardsToBottom(mano);
            mano.clear();
            statusLabel.setText("Maquina " + (idx + 1) + " eliminada!");

            // Actualizar label de la máquina eliminada
            Label machineLabel = getMachineLabel(idx);
            if (machineLabel != null) {
                machineLabel.setText("Maquina " + (idx + 1) + " [ELIMINADA]");
                machineLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");
            }

            if (hayGanador()) {
                mostrarGanador();
                return;
            }

            siguienteMaquina();
            return;
        }

        // Jugar la carta
        Card card = mano.remove(cartaJugada);
        tableSum += valorUsado;
        lastTableCard = card;

        tableCardLabel.setText("Carta: " + card.toString());
        tableSumLabel.setText("Suma: " + tableSum);

        // Robar carta del mazo
        if (!mazo.isEmpty()) {
            mano.add(mazo.drawCard());
        }

        statusLabel.setText("Maquina " + (idx + 1) + " jugó " + card.toString());

        siguienteMaquina();
    }

    /**
     * Pasa al siguiente jugador (máquina o humano).
     */
    private void siguienteMaquina() {
        int siguiente = buscarSiguienteMaquinaViva(turnoMaquinaActual + 1);

        // Si no hay más máquinas vivas después de esta, volver al humano
        if (siguiente == -1 || siguiente <= turnoMaquinaActual) {
            // Todas las máquinas ya jugaron este ciclo
            if (humanoVivo) {
                turnoHumano = true;
                showPlayerHand();
                statusLabel.setText("Tu turno");
            } else {
                // Humano eliminado, seguir con máquinas
                turnoMaquinaActual = buscarSiguienteMaquinaViva(0);
                if (turnoMaquinaActual != -1) {
                    PauseTransition delay = new PauseTransition(Duration.seconds(2 + Math.random() * 2));
                    delay.setOnFinished(e -> jugarMaquina());
                    delay.play();
                }
            }
            return;
        }

        // Siguiente máquina juega
        turnoMaquinaActual = siguiente;
        PauseTransition delay = new PauseTransition(Duration.seconds(2 + Math.random() * 2));
        delay.setOnFinished(e -> jugarMaquina());
        delay.play();
    }

    /**
     * Busca la siguiente máquina viva a partir de un índice.
     * @return índice de la máquina viva, o -1 si no hay más.
     */
    private int buscarSiguienteMaquinaViva(int desde) {
        for (int i = desde; i < numMaquinas; i++) {
            if (maquinasVivas.get(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Verifica si una mano tiene al menos una carta jugable.
     */
    private boolean puedeJugar(List<Card> mano) {
        for (Card c : mano) {
            if (tableSum + c.getGameValue() <= 50) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cuenta cuántos jugadores siguen vivos (humano + máquinas).
     */
    private int contarVivos() {
        int count = 0;
        if (humanoVivo) count++;
        for (boolean viva : maquinasVivas) {
            if (viva) count++;
        }
        return count;
    }

    /**
     * Verifica si ya hay un ganador.
     */
    private boolean hayGanador() {
        return contarVivos() <= 1;
    }

    /**
     * Muestra al ganador y deshabilita el juego.
     */
    private void mostrarGanador() {
        String ganador;
        if (humanoVivo) {
            ganador = "FELICIDADES! Ganaste el juego!";
        } else {
            for (int i = 0; i < numMaquinas; i++) {
                if (maquinasVivas.get(i)) {
                    ganador = "Maquina " + (i + 1) + " ha ganado!";
                    statusLabel.setText(ganador);
                    break;
                }
            }
            return;
        }
        statusLabel.setText(ganador);
        turnoHumano = false;
        showPlayerHand();
    }

    private Label getMachineLabel(int index) {
        switch (index) {
            case 0: return machine1Label;
            case 1: return machine2Label;
            case 2: return machine3Label;
            default: return null;
        }
    }

    /**
     * Muestra un diálogo para que el jugador elija si su As vale 1 o 10.
     * @return 1 o 10 según la elección del jugador
     */
    private int preguntarValorAs() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("As");
        alert.setHeaderText("¿Cuánto vale tu As?");
        alert.setContentText("Elige el valor del As:");

        ButtonType btn1 = new ButtonType("1");
        ButtonType btn10 = new ButtonType("10");
        alert.getButtonTypes().setAll(btn1, btn10);

        java.util.Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btn10) {
            return 10;
        }
        return 1;
    }
}
