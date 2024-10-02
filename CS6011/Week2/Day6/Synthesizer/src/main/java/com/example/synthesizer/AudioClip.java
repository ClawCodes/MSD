package com.example.synthesizer;

import java.util.Arrays;
import javax.sound.sampled.*;

public class AudioClip {
    private static final double duration = 2.0;
    public static final int sampleRate = 44100;
    private byte[] clip = new byte[sampleRate * (short)duration];
    private static final int maxSampleIndex = sampleRate - 1;

    public AudioClip() {};
    public AudioClip(AudioClip clip) {
        this.clip = clip.getData();
    }

    public void combine(AudioClip clip){
        for (int index = 0; index < maxSampleIndex; index++) {
            int thisValue = this.getSample(index);
            int otherValue = clip.getSample(index);
            int summation = thisValue + otherValue;

            // Clamp
            if (summation > Short.MAX_VALUE) {
                summation = Short.MAX_VALUE;
            } else if (summation < Short.MIN_VALUE) {
                summation = Short.MIN_VALUE;
            }

            this.setSample(index, (short)summation);
        }
    }

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
        Clip systemClip = AudioSystem.getClip();
        AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );
        systemClip.open( format16, clip, 0, clip.length );

        systemClip.start();
        systemClip.loop(2);

        while( systemClip.getFramePosition() < AudioClip.sampleRate || systemClip.isActive() || systemClip.isRunning() ){
            // Do nothing while we wait for the note to play.
        }

        systemClip.close();
    }
}