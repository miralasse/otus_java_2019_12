package ru.otus.l08;

/**
 * Demo.
 *
 * @author Evgeniya_Yanchenko
 */
public class Demo {

    public static void main(String[] args) {
        Logging logClass = IoC.createLoggingClass();
        logClass.calculationWithLogging(6);
        logClass.calculationWithoutLogging(6);
    }
}
