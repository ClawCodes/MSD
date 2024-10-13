package com.example.synthesizer.widgets;

import com.example.synthesizer.Mixer;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class MixerWidget extends AudioComponentWidget {
    public MixerWidget(AnchorPane pane, String name) {
        super(new Mixer(), pane, name);
    }

    public void setInputLine(LineWidget inputLine){
        inputLines_.add(inputLine);
    }

    // TODO: Add remove line

    public ArrayList<LineWidget> inputLines_ = new ArrayList<>();
}