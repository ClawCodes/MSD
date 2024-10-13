package com.example.synthesizer.widgets;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Speaker extends MixerWidget {
    public Speaker(AnchorPane pane) {
        super(pane, "speaker");
        baseLayout_.getChildren().remove(leftSide_);
        baseLayout_.getChildren().remove(rightSide_);
        baseLayout_.getChildren().remove(center_);

        this.setLayoutX(600);
        this.setLayoutY(400);

        Circle circle = new Circle(10);
        circle.setFill(Color.BLUE);
        baseLayout_.getChildren().add(circle);
        makeUndraggable();
    }

    private void makeUndraggable(){
        this.setOnMousePressed(mouseEvent -> {});

        this.setOnMouseDragged(mouseEvent -> {});
    }

    public boolean inputContains(double x, double y){
        // Get input widget circle
        Node input = baseLayout_.getChildren().getFirst();
        return input.contains(input.sceneToLocal(x, y));
    }

    public boolean outputContains(double x, double y){
        return false;
    }

    public boolean IOContains(double x, double y){
        return inputContains(x, y) | outputContains(x, y);
    }
}
