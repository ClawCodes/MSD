package com.example.synthesizer;

public class VolumeAdjuster implements AudioComponent{
    AudioClip input;
    AudioClip output = new AudioClip();
    int scale = 1;

    VolumeAdjuster(){
        input = null;
    }

    VolumeAdjuster(AudioClip input){
        this.input = input;
    }

    VolumeAdjuster(AudioClip input, int scale){
        this.input = input;
        this.scale = scale;
    }

    void setScale(int scale){this.scale = scale;}

    public void setOutput(){
        byte[] clipArray = input.getData();
        for (int i = 0; i < AudioClip.sampleRate; i++){
            this.output.setSample(i, (short)(clipArray[i] * scale));
        }
    }

    @Override
    public AudioClip getClip() {
        this.setOutput();
        return this.output;
    }

    @Override
    public boolean hasInput() {
        return input != null;
    }

    @Override
    public void connectInput(AudioClip clip) {
        input = clip;
    }
}
