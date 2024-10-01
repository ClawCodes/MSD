package com.example.synthesizer;

import java.util.Arrays;
import javax.sound.sampled.*;

public class AudioClip {
    private static final double duration = 2.0;
    public static final int sampleRate = 44100;
    private byte[] clip = new byte[sampleRate * (short)duration];
    private static final int maxSampleIndex = sampleRate - 1;

    public int getSample(int index) {
        if (index > maxSampleIndex) {
            throw new IllegalArgumentException(String.format("Index out of bounds, cannot set index %s." +
                    " Max index is %s.", index, maxSampleIndex));
        }
        int startIdx = index * 2;
        int significantByte = clip[startIdx + 1];
        int leastByte = clip[startIdx] & 0xFF;

        return (significantByte << 8) | leastByte;
    }

    public void setSample(int index, short value) {
        if (index > maxSampleIndex) {
            throw new IllegalArgumentException(String.format("Cannot set index %s." +
                    " The maximum index that can be set is %s.", index, maxSampleIndex));
        }

        int startIdx = index * 2;

        clip[startIdx] = (byte)value;
        clip[startIdx + 1] = (byte)(value >> 8);
    }

    public byte[] getData() {
        return Arrays.copyOf(clip, clip.length);
    }

    public void playClip() throws LineUnavailableException {
        Clip c = AudioSystem.getClip();
        AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );
        c.open( format16, clip, 0, clip.length );

        c.start();
        c.loop(2);

        while( c.getFramePosition() < AudioClip.sampleRate || c.isActive() || c.isRunning() ){
            // Do nothing while we wait for the note to play.
        }

        c.close();
    }
}