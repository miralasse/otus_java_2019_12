package ru.otus.l09.framework;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * ru.otus.l09.framework.TestRunner.
 *
 * @author Evgeniya_Yanchenko
 */
@NoArgsConstructor(access = PRIVATE)
public class TestRunner {

    public static final String EMPTY_CLASSNAME = "Class name is null";
    private Class<?> aClass;
    private Method methodBeforeClass;
    private Method methodAfterClass;
    private Method methodBeforeEach;
    private Method methodAfterEach;
    private List<Method> testMethods;
    private int successfulMethodsCount;
    private int failedMethodsCount;

    public static void run(String className) {
        new TestRunner().runTestsForClass(className);
    }

    @SneakyThrows
    private void runTestsForClass(String className) {
        if (isNullOrEmpty(className)) {
            throw new IllegalArgumentException(EMPTY_CLASSNAME);
        }
        aClass = Class.forName(className);
        methodBeforeClass = findAnnotatedMethod(MyBeforeClass.class);
        methodAfterClass = findAnnotatedMethod(MyAfterClass.class);
        methodBeforeEach = findAnnotatedMethod(MyBeforeEach.class);
        methodAfterEach = findAnnotatedMethod(MyAfterEach.class);
        testMethods = findAnnotatedMethods(MyTest.class);

        runTestMethods();
        printResults();
    }

    private void runTestMethods() throws InvocationTargetException, IllegalAccessException,
            NoSuchMethodException, InstantiationException {

        methodBeforeClass.invoke(null);

        for (Method testMethod : testMethods) {
            Object testClassInstance = aClass.getConstructor().newInstance();
            methodBeforeEach.invoke(testClassInstance);
            try {
                testMethod.invoke(testClassInstance);
                successfulMethodsCount++;
            } catch (Exception e) {
                failedMethodsCount++;
            }
            methodAfterEach.invoke(testClassInstance);
        }

        methodAfterClass.invoke(null);
    }

    private Method findAnnotatedMethod(Class<?> annotationType) {
        Method[] methods = aClass.getMethods();
        return Arrays.stream(methods)
                .filter(method ->
                        Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(annotation -> annotation.annotationType().equals(annotationType))
                )
                .findFirst()
                .orElse(null);
    }

    private List<Method> findAnnotatedMethods(Class<?> annotationType) {
        Method[] methods = aClass.getMethods();
        return Arrays.stream(methods)
                .filter(method ->
                        Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(annotation -> annotation.annotationType().equals(annotationType))
                )
                .collect(toList());
    }

    private void printResults() {
        System.out.println(String.format("Всего тестов: %d", testMethods.size()));
        System.out.println(String.format("Выполнено успешно: %d", successfulMethodsCount));
        System.out.println(String.format("Провалено: %d", failedMethodsCount));
    }

}
