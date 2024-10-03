package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SineWaveTest {
    @Test
    void testGetClip() {
        SineWave wave = new SineWave();
        AudioClip clip = wave.getClip();

        short expectedLastElement = -1026;
        short expectedFirstElement = 0;
        Assertions.assertEquals(expectedLastElement, clip.getSample(AudioClip.sampleRate - 1));
        Assertions.assertEquals(expectedFirstElement, clip.getSample(0));
    }
}