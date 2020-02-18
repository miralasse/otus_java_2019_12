package ru.otus.l09.framework;

/**
 * ExampleTest.
 *
 * @author Evgeniya_Yanchenko
 */
public class ExampleTest {

    public static final String INITIAL_SET_UP_TEXT = "initial setUp worked";
    public static final String FINAL_TEAR_DOWN_TEXT = "final tearDown worked";

    static String initialSetUpCheck;
    static String finalTearDownCheck;
    static int beforeEachCount = 0;
    static int afterEachCount = 0;
    static int a = 10;
    static int b = 7;
    static int sum = 0;
    static int difference = 0;
    static int product = 0;

    @MyBeforeClass
    public static void initialSetUp() {
        initialSetUpCheck = INITIAL_SET_UP_TEXT;
    }

    @MyAfterClass
    public static void finalTearDown() {
        finalTearDownCheck = FINAL_TEAR_DOWN_TEXT;
    }

    @MyBeforeEach
    public void setUp() {
        beforeEachCount++;
    }

    @MyAfterEach
    public void tearDown() {
        afterEachCount++;
    }

    @MyTest
    public void testAdd() {
        sum = a + b;
    }

    @MyTest
    public void testSubstract() {
       difference = a - b;
    }

    @MyTest
    public void testMultiply() {
        product = a * b;
    }

}
