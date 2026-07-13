package com.univalle._0zo;

import javafx.application.Application;
import com.univalle._0zo.view.GameView;
import javafx.stage.Stage;

import java.io.IOException;

public class _0zoApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GameView gameView = new GameView();
        gameView.show();
    }
}
