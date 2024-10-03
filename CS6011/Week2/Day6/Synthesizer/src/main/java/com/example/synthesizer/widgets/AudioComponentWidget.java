package com.example.synthesizer.widgets;

import com.example.synthesizer.AudioComponent;
import com.example.synthesizer.SynthesizeApplication;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class AudioComponentWidget extends Pane {

    public AudioComponentWidget(AudioComponent audioComponent, AnchorPane pane, String name) {
        audioComponent_ = audioComponent;
        parent_ = pane;
        name_ = name;

        // Main box
        baseLayout_ = new HBox();
        baseLayout_.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 3;");
        this.getChildren().add(baseLayout_);

        // Right side
        VBox rightSide = new VBox();
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPadding(new Insets(3));
        rightSide.setSpacing(5);

        Button close = new Button("x");
        close.setOnAction(e -> destroyWidget());
        Circle circle = new Circle(10);
        circle.setFill(Color.BLUE);
        rightSide.getChildren().addAll(close, circle);


        // Left side
        VBox leftSide = new VBox();
        Label label = new Label(name_);
        label.setAlignment(Pos.CENTER);
        Slider slider = new Slider();
        leftSide.getChildren().addAll(label, slider);

        baseLayout_.getChildren().addAll(leftSide, rightSide);

        // TODO: Add left side for upstream connection
        this.setLayoutX(50);
        this.setLayoutY(100);
    }

    private void destroyWidget() {
        parent_.getChildren().remove(this);
        SynthesizeApplication.removeWidget(this);
    }

    public AudioComponent getAudioComponent() {
        return audioComponent_;
    }

    private AnchorPane parent_;
    private HBox baseLayout_;
    private AudioComponent audioComponent_;

    // TODO: fill once you draw lines
    private AudioComponentWidget childConnection;
    private AudioComponentWidget parentConnection;

    private String name_;
    private Label nameLabel_;
}
