package com.example.synthesizer.widgets;

import com.example.synthesizer.Mixer;
import com.example.synthesizer.SynthesizeApplication;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class MixerWidget extends AudioComponentWidget {
    public MixerWidget(AnchorPane pane, String name) {
        super(new Mixer(), pane, name);
    }

    public void setInputLine(LineWidget inputLine){
        inputLines_.add(inputLine);
    }

    // TODO: Handle multi line removal
    public void removeInputLine(LineWidget line){
        getAudioComponent().removeInput(line.getOutputWidget().getAudioComponent());
        inputLines_.remove(line);
    }

    public ArrayList<LineWidget> inputLines_ = new ArrayList<>();
}