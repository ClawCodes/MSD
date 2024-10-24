package com.example.synthesizer;

public class SineWave implements AudioComponent {
    private double frequency;
    private int maxValue;
    AudioComponent input = null;

    public SineWave() {
        frequency = 440;
        maxValue = Short.MAX_VALUE / 2;
    }

    SineWave(double frequency) {
        this.frequency = frequency;
        this.maxValue = Short.MAX_VALUE / 2;
    };

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    @Override
    public AudioClip getClip() {
        AudioClip clip = new AudioClip();
        if (hasInput())
            clip = input.getClip();
        for (int i = 0; i < AudioClip.sampleRate; i++){
            double setValue = (maxValue * Math.sin((2*Math.PI*frequency * i / AudioClip.sampleRate)));
            clip.setSample(i, (short) setValue);
        }
        // Combine with input if it exists
        if (this.hasInput()){
            clip.combine(input.getClip());
        }
        return clip;
    }

    @Override
    public boolean hasInput() {
        return this.input != null;
    }

    @Override
    public boolean isPlayable() {
        // Will always produce a default clip
        return true;
    }

    @Override
    public void connectInput(AudioComponent component) {
        input = component;
    }

    @Override
    public void removeInput(AudioComponent component) {
        if (this.input == component)
            input = null;
    }
}
