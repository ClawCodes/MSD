package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;

class VFSineWaveTest {
    @Test
    void testLinearRamp() throws LineUnavailableException {
        VFSineWave VFWave = new VFSineWave();
        VFWave.getClip().playClip();
    }
}