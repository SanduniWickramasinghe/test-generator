package com.example.testgenerator;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

@Slf4j
public class ClassScanner {
    public static Set<Class<?>> getClassesInPackage(String packageName) {
//        Reflections reflections = new Reflections(packageName);//, Scanners.SubTypes);
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackage(packageName)
                        .addScanners(Scanners.SubTypes)
        );
        log.info("Getting Classes In Package. Package name : {}", packageName);
        return reflections.getSubTypesOf(Object.class);
    }
}

