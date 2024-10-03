package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;

class LinearRampTest {
    @Test
    void testLinearRamp() throws LineUnavailableException {
        LinearRamp linearRamp = new LinearRamp();
        AudioClip c = linearRamp.getClip();
        c.playClip();
    }
}