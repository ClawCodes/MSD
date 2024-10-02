package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.stream.IntStream;

class AudioClipTest {
    @Test
    public void testGetData(){
        AudioClip audioClip = new AudioClip();
        Assertions.assertEquals(44100 * 2, audioClip.getData().length);
    }

    @Test
    public void testGetSample(){
        AudioClip audioClip = new AudioClip();
        // The array should have defaults
        Assertions.assertEquals(0, audioClip.getSample(0));
        Assertions.assertEquals(0, audioClip.getSample(200));
        Assertions.assertEquals(0, audioClip.getSample(44099));
    }

    @Test
    public void testSetData(){
        AudioClip audioClip = new AudioClip();
        // Create an inclusive range of all short values to test
        ArrayList<Short> shortRange = new ArrayList<>();
        IntStream.rangeClosed(Short.MIN_VALUE, Short.MAX_VALUE).forEach(i -> shortRange.add((short) i));
        for (short value : shortRange) {
            audioClip.setSample(0, value);
            Assertions.assertEquals(value, audioClip.getSample(0));
        }
    }

    @Test
    public void testPlayClip() throws LineUnavailableException {
        SineWave wave = new SineWave();
        AudioClip clip = wave.getClip();
        clip.playClip();
    }

    @Test
    public void testCombine() {
        AudioClip one = new AudioClip();
        one.setSample(0, (short)1);
        one.setSample(1, (short)2);

        AudioClip two = new AudioClip();
        two.setSample(0, (short)2);
        two.setSample(1, (short)3);

        one.combine(two);

        Assertions.assertEquals(3, one.getSample(0));
        Assertions.assertEquals(5, one.getSample(1));
    }

    @Test
    public void testAll(){
        this.testGetData();
        this.testGetSample();
        this.testSetData();
        System.out.println("All tests passed!");
    }
}