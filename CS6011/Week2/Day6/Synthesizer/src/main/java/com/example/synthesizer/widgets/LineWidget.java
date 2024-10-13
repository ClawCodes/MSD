package com.example.synthesizer.widgets;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

public class LineWidget extends Line {
    public LineWidget(double startX, double startY, double endX, double endY, AnchorPane parent){
        super(startX, startY, endX, endY);
        parent_ = parent;
        parent_.getChildren().add(this);
    }

    public void deleteLine(){
        // TODO:
        // Remove outputWidget input connection and reference to this line
        // Remove inputWidget's reference to this line
        // Remove this line from the anchor pane
        parent_.getChildren().remove(this);
    }

    private AudioComponentWidget inputWidget_;
    private AudioComponentWidget outputWidget_;
    private AnchorPane parent_;
}
