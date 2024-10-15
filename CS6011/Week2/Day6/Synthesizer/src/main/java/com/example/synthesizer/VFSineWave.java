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

        if (hasInput())
            outClip.combine(input.getClip());

        return outClip;
    }

    public void setMaxValue(Short maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public boolean hasInput() {
        return !(input == null);
    }

    @Override
    public boolean isPlayable() {
        return hasInput();
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
