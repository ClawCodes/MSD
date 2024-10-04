package com.example.synthesizer;

import java.util.ArrayList;

public class Mixer implements AudioComponent {
    private static final int maxConnections = 10;
    private ArrayList<AudioComponent> components = new ArrayList<>();

    @Override
    public AudioClip getClip() {
        // Sum all waves
        AudioClip summedClip = new AudioClip(components.getFirst().getClip());
        for (int i = 1; i < components.size(); i++) {
            summedClip.combine(components.get(i).getClip());
        }

        return summedClip;
    }

    @Override
    public boolean hasInput() {
        return !components.isEmpty();
    }

    @Override
    public boolean isPlayable() {
        return hasInput();
    }

    @Override
    public void connectInput(AudioComponent clip) {
        if (components.size() < maxConnections) {
            components.add(clip);
        } else {
            throw new RuntimeException(String.format("Too many connections." +
                    " A Mixer can only accept %s connections.", maxConnections));
        }
    }

    public void remove(AudioComponent component) {
        components.remove(component);
    }
}
