package com.example.synthesizer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface AudioComponent {
    AudioClip getClip();

    boolean hasInput();

    void connectInput(AudioComponent clip);

    default void setWithMethod(String method, Object value, Class paramType) {
        try {
            Method methodObj = this.getClass().getMethod(method, paramType);
            methodObj.invoke(this, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(("Could not set value using: " + method + "\n" + e.getMessage()));
        }
    }
}
