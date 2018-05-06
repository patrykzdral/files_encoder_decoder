package com.pwr.encoder.fxfiles.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getClassLoader().getResource("fxml/main.fxml"));
            loader.load();
            Parent root = loader.getRoot();

            primaryStage.setTitle("Files Encoder");
            primaryStage.getIcons().add(new Image("/image/encoder.png"));
            primaryStage.setMinWidth(610);
            primaryStage.setMinHeight(390);
            primaryStage.setScene(new Scene(root, 679, 493));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException ioEcx) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ioEcx);
        }
    }


    public void stop() {

        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
