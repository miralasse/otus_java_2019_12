package ru.otus.l09.framework;

import static com.google.common.base.Strings.isNullOrEmpty;
import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ru.otus.l09.framework.TestRunner.
 *
 * @author Evgeniya_Yanchenko
 */
@NoArgsConstructor(access = PRIVATE)
public class TestRunner {

    public static final String EMPTY_CLASSNAME = "Class name is null";
    private static Class<?> aClass;
    private static Method methodBeforeClass;
    private static Method methodAfterClass;
    private static Method methodBeforeEach;
    private static Method methodAfterEach;
    private static List<Method> testMethods;


    @SneakyThrows
    public static void run(String className) {

        if (isNullOrEmpty(className)) {
            throw new IllegalArgumentException(EMPTY_CLASSNAME);
        }
        aClass = Class.forName(className);
        methodBeforeClass = findAnnotatedMethod(MyBeforeClass.class);
        methodAfterClass = findAnnotatedMethod(MyAfterClass.class);
        methodBeforeEach = findAnnotatedMethod(MyBeforeEach.class);
        methodAfterEach = findAnnotatedMethod(MyAfterEach.class);
        testMethods = findAnnotatedMethods(MyTest.class);

        runTests();
    }

    @SneakyThrows
    private static void runTests() {

        methodBeforeClass.invoke(null);

        for (Method testMethod : testMethods) {
            Object testClassInstance = aClass.getConstructor().newInstance();
            methodBeforeEach.invoke(testClassInstance);
            testMethod.invoke(testClassInstance);
            methodAfterEach.invoke(testClassInstance);
        }

        methodAfterClass.invoke(null);
    }

    private static Method findAnnotatedMethod(Class<?> annotationType) {
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(annotationType)) {
                    return method;
                }
            }
        }
        return null;
    }

    private static List<Method> findAnnotatedMethods(Class<?> annotationType) {
        Method[] methods = aClass.getMethods();
        List<Method> annotatedMethods = new ArrayList<>();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(annotationType)) {
                    annotatedMethods.add(method);
                }
            }
        }
        return annotatedMethods;
    }

}
