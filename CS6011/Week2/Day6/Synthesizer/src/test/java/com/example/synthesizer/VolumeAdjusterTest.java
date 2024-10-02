package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;

class VolumeAdjusterTest {
    @Test
    void testGetClip() throws LineUnavailableException {
        SineWave wave = new SineWave();
        VolumeAdjuster adjuster = new VolumeAdjuster();
        adjuster.connectInput(wave);

        Assertions.assertEquals(0, wave.getClip().getSample(0));
        Assertions.assertEquals(0, adjuster.getClip().getSample(0));

        Assertions.assertEquals(-1026, wave.getClip().getSample(AudioClip.sampleRate - 1));
        Assertions.assertEquals(-513, adjuster.getClip().getSample(AudioClip.sampleRate - 1));
    }
}