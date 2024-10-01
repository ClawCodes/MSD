package com.example.synthesizer;

public class SineWave implements AudioComponent {
    private int frequency;
    private int maxValue;
    AudioClip input = null;

    SineWave() {
        frequency = 440;
        maxValue = Short.MAX_VALUE / 2;
    }

    SineWave(int frequency) {
        this.frequency = frequency;
        this.maxValue = Short.MAX_VALUE / 2;;
    };

    SineWave(int frequency, short maxValue) {
        this.frequency = frequency;
        this.maxValue = maxValue;
    };

    @Override
    public AudioClip getClip() {
        AudioClip clip = new AudioClip();
        for (int i = 0; i < AudioClip.sampleRate; i++){
            double setValue = (maxValue * Math.sin((2*Math.PI*frequency * i / AudioClip.sampleRate)));
            clip.setSample(i, (short) setValue);
        }
        return clip;
    }

    @Override
    public boolean hasInput() {
        return this.input != null;
    }

    @Override
    public void connectInput(AudioClip clip) {
        this.input = clip;
    }
}
