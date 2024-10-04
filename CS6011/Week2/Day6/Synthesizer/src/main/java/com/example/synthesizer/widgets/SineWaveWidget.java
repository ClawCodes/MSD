package com.example.synthesizer.widgets;

import com.example.synthesizer.AudioComponent;
import com.example.synthesizer.SineWave;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

public class SineWaveWidget extends AudioComponentWidget{
    public SineWaveWidget(AnchorPane pane, String name) {
        super(new SineWave(), pane, name);
        Slider slider = new Slider();
        slider.setMin(100);
        slider.setMax(1000);
        slider.setOnMouseDragged(e -> updateFrequency(e, slider));
        leftSide_.getChildren().add(slider);
    }

    private void updateFrequency(javafx.scene.input.MouseEvent e, Slider slider) {
        audioComponent_.setByName("frequency", slider.getValue());
    }
}
