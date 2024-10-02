package com.example.examplefx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AnchorPane ap = new AnchorPane();
        VBox menuBox = new VBox();
        menuBox.setStyle("-fx-background-color: lightblue");

        Button btn = new Button();
        btn.setText("Create Sine Wave Component");

        // Have btn do something (create an event)
        btn.setOnAction(event -> handleCreateSW(ap));

        Button btn2 = new Button();
        btn2.setText("Create Mixer Component");

        menuBox.getChildren().add(btn);
        menuBox.getChildren().add(btn2);



        ap.getChildren().add(menuBox);

        Scene scene = new Scene(ap, 320, 240);
        stage.setScene(scene);
        stage.setTitle("Hello");
        stage.show();
    }

    private int swNumber = 0;

    private void handleCreateSW(AnchorPane ap) {
        Button btn = new Button();
        btn.setText("Sine Wave");
        ap.getChildren().add(btn);
        btn.setLayoutX(200 + 100 * swNumber);
        btn.setLayoutY(200);
        swNumber++;
    }

    public static void main(String[] args) {

        launch();

    }
}