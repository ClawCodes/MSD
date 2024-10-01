package com.example.synthesizer;

public class SineWave implements AudioComponent {
    private double frequency;
    private int maxValue;
    AudioComponent input = null;

    SineWave() {
        frequency = 440;
        maxValue = Short.MAX_VALUE / 2;
    }

    SineWave(double frequency) {
        this.frequency = frequency;
        this.maxValue = Short.MAX_VALUE / 2;;
    };

    SineWave(double frequency, short maxValue) {
        this.frequency = frequency;
        this.maxValue = maxValue;
    };

    @Override
    public AudioClip getClip() {
        AudioClip clip = new AudioClip();
        if (hasInput())
            clip = input.getClip();
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
    public void connectInput(AudioComponent component) {
        input = component;
    }
}
