package ru.otus.l08;

/**
 * TestLogging.
 *
 * @author Evgeniya_Yanchenko
 */
public class TestLogging implements Logging{

    @Log
    @Override
    public void calculationWithLogging(int param) {
        System.out.println("2x-result: " + param * 2);
    }

    @Override
    public void calculationWithoutLogging(int param) {
        System.out.println("3x-result: " + param * 3);
    }
}
