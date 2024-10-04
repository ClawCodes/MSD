package com.example.synthesizer.widgets;

import com.example.synthesizer.AudioComponent;
import com.example.synthesizer.SynthesizeApplication;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        rightSide_ = new VBox();
        rightSide_.setAlignment(Pos.CENTER);
        rightSide_.setPadding(new Insets(3));
        rightSide_.setSpacing(5);

        Button close = new Button("x");
        close.setOnAction(e -> destroyWidget());
        Circle circle = new Circle(10);
        circle.setFill(Color.BLUE);
        rightSide_.getChildren().addAll(close, circle);

        // Left side
        leftSide_ = new VBox();
        Label label = new Label(name_);
        label.setAlignment(Pos.CENTER);
        leftSide_.getChildren().addAll(label);

        baseLayout_.getChildren().addAll(leftSide_, rightSide_);

        // TODO: Add left side for upstream connection
        this.setLayoutX(50);
        this.setLayoutY(100);

        makeDraggable();
    }

    protected void setSlider(int min, int max, String memberVar, Class paramType){
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setOnMouseDragged(e -> setWithComponentMethod(e, memberVar, slider, paramType));
        leftSide_.getChildren().add(slider);
    }

    protected void setWithComponentMethod(javafx.scene.input.MouseEvent e, String method, Slider slider, Class paramType) {
        audioComponent_.setWithMethod(method, slider.getValue(), Double.class);
    }

    private void makeDraggable() {
        this.setOnMousePressed(mouseEvent -> {
            startX_ = mouseEvent.getSceneX() - this.getTranslateX();
            startY_ = mouseEvent.getSceneX() - this.getTranslateY();
        });

        this.setOnMouseDragged(mouseEvent -> {
            this.setTranslateX(mouseEvent.getSceneX() - startX_);
            this.setTranslateY(mouseEvent.getSceneY() - startY_);
        });
    }

    private void destroyWidget() {
        parent_.getChildren().remove(this);
        SynthesizeApplication.removeWidget(this);
    }

    public AudioComponent getAudioComponent() {
        return audioComponent_;
    }

    protected AnchorPane parent_;
    protected HBox baseLayout_;
    protected VBox rightSide_;
    protected VBox leftSide_;
    protected AudioComponent audioComponent_;

    // TODO: fill once you draw lines
    private AudioComponentWidget childConnection;
    private AudioComponentWidget parentConnection;

    protected String name_;
    protected Label nameLabel_;

    protected double startX_;
    protected double startY_;
}
