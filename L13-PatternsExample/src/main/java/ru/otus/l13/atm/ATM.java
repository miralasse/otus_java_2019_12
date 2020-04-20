package ru.otus.l13.atm;

import ru.otus.l13.locker.Locker;

import java.util.Set;

/**
 * ATM.
 *
 * @author Evgeniya_Yanchenko
 */
public interface ATM extends ClientInteractingATM, Restorable, Countable, Cloneable {

    void addLocker(Locker locker);

    void addLockers(Set<Locker> lockerSet);

    void removeLocker(Locker locker);

    ATM clone();
}
