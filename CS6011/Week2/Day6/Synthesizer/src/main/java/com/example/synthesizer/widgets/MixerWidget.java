package com.example.synthesizer.widgets;

import com.example.synthesizer.Mixer;
import com.example.synthesizer.SynthesizeApplication;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class MixerWidget extends AudioComponentWidget {
    public MixerWidget(AnchorPane pane, String name) {
        super(new Mixer(), pane, name);
    }

    public void setInputLine(LineWidget inputLine){
        inputLines_.add(inputLine);
    }

    @Override
    public void adjustLines() {
        if (outputLine_ != null) {
            outputLine_.moveLine();
        }
        if (!inputLines_.isEmpty()) {
            for (LineWidget lineWidget : inputLines_) {
                lineWidget.moveLine();
            }
        }
    }

    @Override
    public void removeInputLine(LineWidget line){
        getAudioComponent().removeInput(line.getOutputWidget().getAudioComponent());
        inputLines_.remove(line);
    }

    @Override
    public void destroyWidget() {
        if (outputLine_ != null) {
            outputLine_.remove();
        }
        if (!inputLines_.isEmpty()) {
            for (LineWidget lineWidget : inputLines_) {
                lineWidget.remove();
            }
        }
        parent_.getChildren().remove(this);
        SynthesizeApplication.removeWidget(this);
    }

    public ArrayList<LineWidget> inputLines_ = new ArrayList<>();
}