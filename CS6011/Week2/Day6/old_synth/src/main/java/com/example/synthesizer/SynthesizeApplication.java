package com.example.synthesizer;

import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SynthesizeApplication extends Application {
    @Override
    public void start(Stage stage) {
        AnchorPane anchorPane = new AnchorPane();
        VBox componentMenu = new VBox();
        componentMenu.setStyle("-fx-background-color: rgba(34,128,0,0.66)");

        Button sineWaveBtn = new Button();
        sineWaveBtn.setText("Sine Wave");

        Button mixerBtn = new Button();
        mixerBtn.setText("Mixer");

        componentMenu.getChildren().add(sineWaveBtn);
        componentMenu.getChildren().add(mixerBtn);

        anchorPane.getChildren().add(componentMenu);

        Scene scene = new Scene(anchorPane, 800, 800);
        stage.setScene(scene);
        stage.setTitle("Rad Synth");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}