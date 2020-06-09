package ru.otus.l13.atm;

/**
 * Restorable.
 *
 * @author Evgeniya_Yanchenko
 */
public interface Restorable {

    void saveState();

    void restoreState();
}
