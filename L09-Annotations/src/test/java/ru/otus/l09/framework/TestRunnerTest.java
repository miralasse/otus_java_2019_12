package ru.otus.l09.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.l09.framework.ExampleTest.FINAL_TEAR_DOWN_TEXT;
import static ru.otus.l09.framework.ExampleTest.INITIAL_SET_UP_TEXT;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * TestRunnerTest.
 *
 * @author Evgeniya_Yanchenko
 */
class TestRunnerTest {

    private ExampleTest exampleTest = new ExampleTest();
    private int numberOfTestMethods = 3;

    @AfterEach
    void tearDown() {
        ExampleTest.initialSetUpCheck = null;
        ExampleTest.finalTearDownCheck = null;
        ExampleTest.beforeEachCount = 0;
        ExampleTest.afterEachCount = 0;
        ExampleTest.sum = 0;
        ExampleTest.difference = 0;
        ExampleTest.product = 0;
    }

    @Test
    public void testBeforeClassAnnotation() {
        assertThat(ExampleTest.initialSetUpCheck).isNull();
        TestRunner.run(exampleTest.getClass().getName());
        assertThat(ExampleTest.initialSetUpCheck).isEqualTo(INITIAL_SET_UP_TEXT);
    }

    @Test
    public void testAfterClassAnnotation() {
        assertThat(ExampleTest.finalTearDownCheck).isNull();
        TestRunner.run(exampleTest.getClass().getName());
        assertThat(ExampleTest.finalTearDownCheck).isEqualTo(FINAL_TEAR_DOWN_TEXT);
    }

    @Test
    public void testBeforeEachAnnotation() {
        assertThat(ExampleTest.beforeEachCount).isEqualTo(0);
        TestRunner.run(exampleTest.getClass().getName());
        assertThat(ExampleTest.beforeEachCount).isEqualTo(numberOfTestMethods);
    }

    @Test
    public void testAfterEachAnnotation() {
        assertThat(ExampleTest.afterEachCount).isEqualTo(0);
        TestRunner.run(exampleTest.getClass().getName());
        assertThat(ExampleTest.afterEachCount).isEqualTo(numberOfTestMethods);
    }

    @Test
    public void testTestAnnotation() {
        assertThat(ExampleTest.sum).isEqualTo(0);
        assertThat(ExampleTest.difference).isEqualTo(0);
        assertThat(ExampleTest.product).isEqualTo(0);

        TestRunner.run(exampleTest.getClass().getName());

        assertThat(ExampleTest.sum).isEqualTo(ExampleTest.a + ExampleTest.b);
        assertThat(ExampleTest.difference).isEqualTo(ExampleTest.a - ExampleTest.b);
        assertThat(ExampleTest.product).isEqualTo(ExampleTest.a * ExampleTest.b);
    }
}