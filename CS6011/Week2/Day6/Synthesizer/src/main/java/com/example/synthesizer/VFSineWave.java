package com.example.synthesizer;

public class VFSineWave implements AudioComponent{
    private AudioComponent input = new LinearRamp();
    private short maxValue = Short.MAX_VALUE / 2;

    @Override
    public AudioClip getClip() {
        AudioClip outClip = new AudioClip(input.getClip());
        double phase = 0;
        for (int i = 0; i < AudioClip.sampleRate; i++){
            phase += (2 * Math.PI * outClip.getSample(i) / AudioClip.sampleRate);
            outClip.setSample(i, (short)(maxValue * Math.sin( phase )));
        }
        return outClip;
    }

    @Override
    public boolean hasInput() {
        return !(input == null);
    }

    @Override
    public void connectInput(AudioComponent component) {
        input = component;
    }

    void setInput(AudioComponent input) {
        this.input = input;
    }
}