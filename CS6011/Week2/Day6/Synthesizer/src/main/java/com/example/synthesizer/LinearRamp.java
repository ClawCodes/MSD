package com.example.synthesizer;

public class LinearRamp implements AudioComponent {
    private AudioComponent input;
    private int start;
    private int stop;

    public LinearRamp(){
        this.start = 50;
        this.stop = 2000;
    }

    LinearRamp(int start, int stop) {
        this.start = start;
        this.stop = stop;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public void setStop(Integer stop) {
        this.stop = stop;
    }

    @Override
    public AudioClip getClip() {
        AudioClip outClip = new AudioClip();
        for (int i = 0; i < AudioClip.sampleRate; i++){
            double setValue = (double) (start * (AudioClip.sampleRate - i) + stop * i) / AudioClip.sampleRate;
            outClip.setSample(i, (short)setValue);
        }
        if (hasInput()){
            outClip.combine(input.getClip());
        }
        return outClip;
    }

    @Override
    public boolean hasInput() {
        return !(input == null);
    }

    @Override
    public boolean isPlayable() {
        // Will always produce a default clip
        return true;
    }

    @Override
    public void connectInput(AudioComponent component) {
        this.input = component;
    }
}
