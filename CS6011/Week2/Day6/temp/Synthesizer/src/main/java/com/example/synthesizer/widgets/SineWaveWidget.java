package com.example.synthesizer.widgets;

import com.example.synthesizer.SineWave;
import javafx.scene.layout.AnchorPane;

public class SineWaveWidget extends AudioComponentWidget{
    public SineWaveWidget(AnchorPane pane, String name) {
        super(new SineWave(), pane, name);
        setSlider(100, 1000, "setFrequency", Double.class);
    }
}
