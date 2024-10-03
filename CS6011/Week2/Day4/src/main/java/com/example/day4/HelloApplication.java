package com.example.day4;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Hi :)");

        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(4));
        rightPane.setStyle("-fx-background-color: darkgrey");
        Button waveButton = new Button("Wave");

        root.setRight(rightPane);

    }

    public static void main(String[] args) {
        launch();
    }
}