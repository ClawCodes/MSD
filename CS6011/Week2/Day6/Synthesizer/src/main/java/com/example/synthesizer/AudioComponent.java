package com.example.synthesizer;

import java.lang.reflect.Field;

public interface AudioComponent{
    AudioClip getClip();
    boolean hasInput();
    void connectInput(AudioComponent clip);
    default void setByName(String memberVariable, Object value){
        try {
            Field field = this.getClass().getDeclaredField(memberVariable);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not set field: " + memberVariable);
        }
    }
}
