package com.example.synthesizer.widgets;

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
        // FIXME: not shifting correctly
        double inputTranslateX = inputWidget_.getTranslateX();
        double inputTranslateY = inputWidget_.getTranslateY();
        double outputTranslateX = outputWidget_.getTranslateX();
        double outputTranslateY = outputWidget_.getTranslateY();

        if (startsFromOutput_){
            this.setStartX(this.getStartX() - outputTranslateX);
            this.setStartY(this.getStartY() - outputTranslateY);
            this.setEndX(this.getEndX() - inputTranslateX);
            this.setEndY(this.getEndY() - inputTranslateY);
        } else{
            this.setStartX(this.getStartX() - inputTranslateX);
            this.setStartY(this.getStartY() - inputTranslateY);
            this.setEndX(this.getEndX() - inputTranslateX);
            this.setEndY(this.getEndY() - inputTranslateY);
        }
    }

    private boolean startsFromOutput_ = true;
    private AudioComponentWidget inputWidget_;
    private AudioComponentWidget outputWidget_;
    private AnchorPane parent_;
}
