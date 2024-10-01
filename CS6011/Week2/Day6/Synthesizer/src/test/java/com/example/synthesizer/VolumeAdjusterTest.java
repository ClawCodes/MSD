package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;

class VolumeAdjusterTest {
    @Test
    void testGetClip() throws LineUnavailableException {
        SineWave wave = new SineWave();
        AudioClip clip = wave.getClip();
        VolumeAdjuster adjuster = new VolumeAdjuster(clip);

        adjuster.getClip().playClip();
    }
}