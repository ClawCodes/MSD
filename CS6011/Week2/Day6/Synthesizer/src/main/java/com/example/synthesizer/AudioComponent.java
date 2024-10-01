package com.example.synthesizer;

public interface AudioComponent{
    AudioClip getClip();
    boolean hadInput();
    void connectInput();
}
