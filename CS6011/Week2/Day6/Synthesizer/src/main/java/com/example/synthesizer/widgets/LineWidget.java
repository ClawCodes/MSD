package com.example.synthesizer.widgets;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

public class LineWidget extends Line {
    public LineWidget(double startX, double startY, double endX, double endY, AnchorPane parent){
        super(startX, startY, endX, endY);
        parent_ = parent;
        parent_.getChildren().add(this);
    }

    public void remove(){
        outputWidget_.removeOutputLine(this);
        inputWidget_.removeInputLine(this);
        parent_.getChildren().remove(this);
    }

    public void setInputWidget(AudioComponentWidget widget){
        inputWidget_ = widget;
        inputWidget_.setInputLine(this);
    }

    public void setOutputWidget(AudioComponentWidget widget){
        outputWidget_ = widget;
        outputWidget_.setOutputLine(this);
    }

    public AudioComponentWidget getOutputWidget(){
        return outputWidget_;
    }

    public void connectComponents(){
        inputWidget_.getAudioComponent().connectInput(outputWidget_.getAudioComponent());
    }

    public void setStartsFromOutput(boolean value){
        startsFromOutput_ = value;
    }

    public void moveLine(){
        Node startJack;
        Node endJack;
        if (startsFromOutput_){
            startJack = outputWidget_.getOutputCircle();
            endJack = inputWidget_.getInputCircle();
        } else{
            endJack = outputWidget_.getOutputCircle();
            startJack = inputWidget_.getInputCircle();
        }
        this.setStartX(startJack.getLocalToSceneTransform().getTx());
        this.setStartY(startJack.getLocalToSceneTransform().getTy());
        this.setEndX(endJack.getLocalToSceneTransform().getTx());
        this.setEndY(endJack.getLocalToSceneTransform().getTy());
    }

    private boolean startsFromOutput_ = true;
    private AudioComponentWidget inputWidget_;
    private AudioComponentWidget outputWidget_;
    private AnchorPane parent_;
}
