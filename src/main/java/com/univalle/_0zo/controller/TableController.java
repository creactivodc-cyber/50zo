package com.univalle._0zo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

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

    /**
     * Oculta las máquinas que no fueron seleccionadas.
     *
     * @param numMachines cantidad de máquinas (1, 2 o 3)
     */
    public void setMachines(int numMachines) {
        machine2Label.setVisible(numMachines >= 2);
        machine2Label.setManaged(numMachines >= 2);
        machine3Label.setVisible(numMachines >= 3);
        machine3Label.setManaged(numMachines >= 3);
    }
}
