package ru.otus.appcontainer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Slf4j
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    @SneakyThrows
    private void processConfig(Class<?> configClass) {
        log.info("Start processing of config: " + configClass.getSimpleName());
        checkConfigClass(configClass);
        Object instanceOfConfigClass = configClass.getDeclaredConstructor().newInstance();
        Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .forEach(method -> createComponent(method, instanceOfConfigClass));
    }

    @SneakyThrows
    private void createComponent(Method method, Object instanceOfConfigClass) {
        String componentName = method.getAnnotation(AppComponent.class).name();
        log.info("Start creation of component: " + componentName);
        Object[] args;
        try {
            args = Arrays.stream(method.getParameterTypes())
                    .map(this::getAppComponent)
                    .toArray();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(format("An exception was thrown trying to create %s instance: %s",
                    componentName, e.getMessage()));
        }
        Object component = method.invoke(instanceOfConfigClass, args);

        appComponents.add(component);
        appComponentsByName.put(componentName, component);
        log.info("Finish creation of component: " + componentName);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(component -> componentClass.isInstance(component))
                .findFirst().orElseThrow(
                        () -> new IllegalArgumentException(format("There is no component with class %s", componentClass.getSimpleName())));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        } else {
            throw new IllegalArgumentException((format("There is no component with name %s", componentName)));
        }
    }
}
