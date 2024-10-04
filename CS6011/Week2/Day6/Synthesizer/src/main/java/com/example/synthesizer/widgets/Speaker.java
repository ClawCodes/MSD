package com.example.synthesizer.widgets;

import com.example.synthesizer.AudioComponent;
import com.example.synthesizer.Mixer;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Speaker extends Pane {
    public Speaker(AnchorPane pane) {
        parent_ = pane;

        this.setLayoutX(600);
        this.setLayoutY(400);

        baseLayout_ = new HBox();
        baseLayout_.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 3;");
        this.getChildren().add(baseLayout_);

        Circle circle = new Circle(10);
        circle.setFill(Color.BLUE);
        baseLayout_.getChildren().add(circle);
    }

    public AudioComponent getAudioComponent() {
        return audioComponent_;
    }
    AudioComponent audioComponent_ = new Mixer();
    HBox baseLayout_;
    AnchorPane parent_;
}
