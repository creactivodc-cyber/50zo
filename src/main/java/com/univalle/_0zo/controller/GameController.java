package com.univalle._0zo.controller;

import com.univalle._0zo.view.GameTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {

    @FXML
    private Button btn1Machine;
    @FXML
    private Button btn2Machines;
    @FXML
    private Button btn3Machines;

    @FXML
    public void onSelectMachines(ActionEvent event) throws IOException {
        Button source = (Button) event.getSource();
        int numMachines;

        if (source == btn1Machine) {
            numMachines = 1;
        } else if (source == btn2Machines) {
            numMachines = 2;
        } else {
            numMachines = 3;
        }

        // Abrir la mesa del juego
        GameTableView tableView = new GameTableView(numMachines);
        tableView.show();

        // Cerrar pantalla de selección
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.close();
    }
}
