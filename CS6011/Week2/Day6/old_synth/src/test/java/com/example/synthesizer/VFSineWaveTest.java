package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;

class VFSineWaveTest {
    @Test
    void testVFSineWave() throws LineUnavailableException {
        VFSineWave VFWave = new VFSineWave();
        VFWave.getClip().playClip();
    }

    @Test
    void testVFSineWaveNonDefaultLinearRamp() throws LineUnavailableException {

        VFSineWave VFWave = new VFSineWave();
        VFWave.setInput(new LinearRamp(500, 100000));
        VFWave.getClip().playClip();
    }
}