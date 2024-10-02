package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;

class MixerTest {
    @Test
    void testTwoConnections() throws LineUnavailableException {
        Mixer mixer = new Mixer();
        SineWave sineWave = new SineWave(440);
        SineWave sineWave2 = new SineWave(220);
        SineWave sineWave3 = new SineWave(350);
        SineWave sineWave4 = new SineWave(100);

        mixer.connectInput(sineWave);
        mixer.connectInput(sineWave2);
        mixer.connectInput(sineWave3);
        mixer.connectInput(sineWave4);

        mixer.getClip().playClip();
    }
}