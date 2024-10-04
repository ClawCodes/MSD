package com.example.synthesizer.widgets;

import com.example.synthesizer.Mixer;
import javafx.scene.layout.AnchorPane;

public class MixerWidget extends AudioComponentWidget {
    public MixerWidget(AnchorPane pane, String name) {
        super(new Mixer(), pane, name);
    }
}