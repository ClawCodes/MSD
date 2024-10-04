package com.example.synthesizer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface AudioComponent {
    AudioClip getClip();

    boolean hasInput();

    void connectInput(AudioComponent clip);

    boolean isPlayable();

    default void setWithMethod(String method, Double value, Class<?> paramType) {
        try {
            Object valueToPass = value;
            if (paramType == Integer.class) {
                valueToPass = value.intValue();
            } else if (paramType == Short.class) {
                valueToPass = value.shortValue();
            }
            Method methodObj = this.getClass().getMethod(method, paramType);
            methodObj.invoke(this, paramType.cast(valueToPass));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(("Could not set value using: " + method + "\n" + e.getMessage()));
        }
    }
}
