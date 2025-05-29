package com.example.testgenerator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;

public class TypeExtractor {

    public static Set<Class<?>> extractReferencedTypes(Method method) {
        Set<Class<?>> types = new HashSet<>();

        // Add return type if it’s not primitive or from java.*
        Class<?> returnType = method.getReturnType();
        if (isCustomType(returnType)) {
            types.add(returnType);
        }

        // Add all parameter types
        for (Parameter param : method.getParameters()) {
            Class<?> paramType = param.getType();
            if (isCustomType(paramType)) {
                types.add(paramType);
            }
        }

        return types;
    }

    private static boolean isCustomType(Class<?> clazz) {
        return !clazz.isPrimitive()
                && !clazz.getName().startsWith("java.")
                && !clazz.isArray()
                && !clazz.isInterface()
                && !clazz.isAnnotation();
    }
}
