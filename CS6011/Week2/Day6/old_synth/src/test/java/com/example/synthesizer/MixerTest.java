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

        mixer.connectInput(sineWave);
        mixer.connectInput(sineWave2);

        AudioClip actualClip = mixer.getClip();
        Assertions.assertEquals(1539, actualClip.getSample(1));
        Assertions.assertEquals(-1026, actualClip.getSample(AudioClip.sampleRate - 1));
    }

    @Test
    void testChainingComponents() throws LineUnavailableException {
        Mixer mixer = new Mixer();
        SineWave sineWave = new SineWave(440);
        SineWave sineWave2 = new SineWave(220);

        VolumeAdjuster adjuster = new VolumeAdjuster(sineWave, 0.2);
        VolumeAdjuster adjuster2 = new VolumeAdjuster(sineWave2, 0.2);

        mixer.connectInput(adjuster);
        mixer.connectInput(adjuster2);

        mixer.getClip().playClip();
    }
}