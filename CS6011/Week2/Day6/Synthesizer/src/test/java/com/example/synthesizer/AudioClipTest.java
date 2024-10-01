package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            audioClip.setSample(0, i);
            Assertions.assertEquals(i, audioClip.getSample(0));
        }
    }

    @Test
    public void testAll(){
        this.testGetData();
        this.testGetSample();
        this.testSetData();
        System.out.println("All tests passed!");
    }
}