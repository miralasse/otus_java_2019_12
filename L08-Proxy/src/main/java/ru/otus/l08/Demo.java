package ru.otus.l08;

/**
 * Demo.
 *
 * @author Evgeniya_Yanchenko
 */
public class Demo {

    public static void main(String[] args) {
        Logging logClass = IoC.createLoggingClass();
        logClass.calculation(6);
        logClass.calculation(6, 4);
        logClass.calculationWithoutLogging(6);
    }
}
