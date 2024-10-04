package com.example.synthesizer.widgets;

import com.example.synthesizer.LinearRamp;
import javafx.scene.layout.AnchorPane;

public class LinearRampWidget extends AudioComponentWidget {
    public LinearRampWidget(AnchorPane pane, String name) {
        super(new LinearRamp(), pane, name);
        setSlider(100, 1000, "setStart", Integer.class);
        setSlider(100, 1000, "setStop", Integer.class);
    }
}
