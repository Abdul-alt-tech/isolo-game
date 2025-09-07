package com.isolo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file from the correct path
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
            primaryStage.setTitle("Isolo - Traditional Zambian Board Game");
            primaryStage.setScene(new Scene(root, 900, 600));
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(550);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}