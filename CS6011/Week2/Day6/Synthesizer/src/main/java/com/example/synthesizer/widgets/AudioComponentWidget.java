package com.example.synthesizer.widgets;

import com.example.synthesizer.AudioComponent;
import com.example.synthesizer.SynthesizeApplication;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Transform;

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

        leftSide_ = new VBox();
        leftSide_.setAlignment(Pos.CENTER);
        leftSide_.setPadding(new Insets(3));
        leftSide_.setSpacing(5);

        Circle upstreamInput = new Circle(10);
        upstreamInput.setFill(Color.BLUE);
        leftSide_.getChildren().add(upstreamInput);

        // Left side
        center_ = new VBox();
        Label label = new Label(name_);
        label.setAlignment(Pos.CENTER);
        center_.getChildren().addAll(label);

        baseLayout_.getChildren().addAll(leftSide_, center_, rightSide_);

        this.setLayoutX(50);
        this.setLayoutX(100);

        makeDraggable();
    }

    public boolean inputContains(double x, double y){
        // Get input widget circle
        Node input = leftSide_.getChildren().getFirst();
        return input.contains(input.sceneToLocal(x, y));
    }

    public boolean outputContains(double x, double y){
        // Get output widget circle
        Node output = rightSide_.getChildren().get(1);
        return output.contains(output.sceneToLocal(x, y));
    }

    public boolean IOContains(double x, double y){
        return inputContains(x, y) | outputContains(x, y);
    }

    protected void setSlider(int min, int max, String memberMethod, Class<?> paramType){
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setOnMouseDragged(e -> setWithComponentMethod(e, memberMethod, slider, paramType));
        center_.getChildren().add(slider);
    }

    protected void setWithComponentMethod(javafx.scene.input.MouseEvent e, String method, Slider slider, Class<?> paramType)
    {
        audioComponent_.setWithMethod(method, slider.getValue(), paramType);
    }

    private void makeDraggable() {
        this.setOnMousePressed(mouseEvent -> {
            pressedX_ = mouseEvent.getSceneX();
            pressedY_ = mouseEvent.getSceneY();
            startX_ = pressedX_ - this.getTranslateX();
            startY_ = pressedY_ - this.getTranslateY();
        });

        this.setOnMouseDragged(mouseEvent -> {
            if (!this.IOContains(pressedX_, pressedY_)) {
                this.setTranslateX(mouseEvent.getSceneX() - startX_);
                this.setTranslateY(mouseEvent.getSceneY() - startY_);
            }
        });
    }

    private void destroyWidget() {
        parent_.getChildren().remove(this);
        SynthesizeApplication.removeWidget(this);
    }

    public AudioComponent getAudioComponent() {
        return audioComponent_;
    }

    // TODO: Adjust input and output lines based on drag movement from widget drag
    private void adjustLines(){}

    public Line getInputLine(){
        return inputLine;
    }

    public Line getOutputLine(){
        return outputLine;
    }

    protected AnchorPane parent_;
    protected HBox baseLayout_;
    protected VBox rightSide_;
    protected VBox center_;
    protected VBox leftSide_;
    protected AudioComponent audioComponent_;

    protected String name_;

    // TODO: Keep?
    protected Line inputLine;
    protected Line outputLine;

    // Member variables for widget dragging
    protected double startX_;
    protected double startY_;
    protected double pressedX_;
    protected double pressedY_;
}
