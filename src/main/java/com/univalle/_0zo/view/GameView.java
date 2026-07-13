package com.univalle._0zo.view;

import com.univalle._0zo.controller.GameController;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;


public class GameView extends Stage{
    private GameController gameController;

    public GameView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/univalle/_0zo/view/game-view.fxml"));
        Parent root = loader.load();

        gameController = loader.getController();

        Scene scene = new Scene(root);
        setTitle("50zo");
        setScene(scene);
        setResizable(false);
        gameController.initialize();
    }
        
    
}
