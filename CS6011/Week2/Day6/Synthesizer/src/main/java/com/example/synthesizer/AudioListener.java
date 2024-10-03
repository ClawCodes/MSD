package com.example.synthesizer;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class AudioListener implements LineListener {
    public AudioListener(Clip clip) {
        clip_ = clip;
    }
    @Override
    public void update(LineEvent event) {
        System.out.println(event.toString());
        if (event.getType() == LineEvent.Type.STOP) {
            clip_.stop();
        }
    }

    private Clip clip_;
}
