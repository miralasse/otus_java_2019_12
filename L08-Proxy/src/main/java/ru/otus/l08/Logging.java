package ru.otus.l08;

/**
 * Logging.
 *
 * @author Evgeniya_Yanchenko
 */
public interface Logging {

    void calculation(int param);

    void calculation(int param, int secondParam);

    void calculationWithoutLogging(int param);
}
