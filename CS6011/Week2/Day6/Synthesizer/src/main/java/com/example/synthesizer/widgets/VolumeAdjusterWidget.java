package com.example.synthesizer.widgets;

import com.example.synthesizer.VolumeAdjuster;
import javafx.scene.layout.AnchorPane;

public class VolumeAdjusterWidget extends AudioComponentWidget {
    public VolumeAdjusterWidget(AnchorPane pane, String name) {
        super(new VolumeAdjuster(), pane, name);
        setSlider(0, 100, "scale", Integer.class);
    }
}
