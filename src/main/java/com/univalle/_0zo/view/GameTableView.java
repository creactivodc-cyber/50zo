package com.univalle._0zo.view;

import com.univalle._0zo.controller.TableController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Vista de la mesa del juego.
 * Se abre cuando el jugador selecciona la cantidad de máquinas.
 */
public class GameTableView extends Stage {

    public GameTableView(int numMachines) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/univalle/_0zo/view/game-table.fxml"));
        Parent root = loader.load();

        TableController controller = loader.getController();
        controller.setMachines(numMachines);

        setTitle("50ZO - Mesa");
        setScene(new Scene(root));
        setResizable(false);
    }
}
