package com.example.synthesizer;

import java.util.Arrays;

public class AudioClip {
    private static final double duration = 2.0;
    private static final int sampleRate = 44100;
    private byte[] clip = new byte[sampleRate * (short)duration];

    public int getSample(int index) {
        if (index > clip.length - 1) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        int startIdx = index * 2;
        int significantByte = clip[startIdx + 1];
        int leastByte = clip[startIdx] & 0xFF;

        return (significantByte << 8) | leastByte;
    }

    public void setSample(int index, short value) {
        if (index > clip.length - 1) {
            throw new IllegalArgumentException("Cannot set index " + index +
                    ". The current clip length is " + clip.length + ".");
        }

        int startIdx = index * 2;

        clip[startIdx] = (byte)value;
        clip[startIdx + 1] = (byte)(value >> 8);
    }

    public byte[] getData() {
        return Arrays.copyOf(clip, clip.length);
    }
}