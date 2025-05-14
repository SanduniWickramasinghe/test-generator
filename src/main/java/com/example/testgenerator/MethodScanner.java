package com.example.testgenerator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodScanner {

    public static List<Method> getPublicMethods(Class<?> clazz) {
        List<Method> publicMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (java.lang.reflect.Modifier.isPublic(method.getModifiers())) {
                publicMethods.add(method);
            }
        }
        return publicMethods;
    }
}
