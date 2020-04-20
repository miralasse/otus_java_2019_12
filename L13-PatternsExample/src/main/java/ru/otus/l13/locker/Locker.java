package ru.otus.l13.locker;

import ru.otus.l13.BanknoteType;
import ru.otus.l13.atm.Countable;

/**
 * Locker.
 *
 * @author Evgeniya_Yanchenko
 */

public interface Locker extends Cloneable, Countable {

    BanknoteType getBanknoteType();

    int getNumberOfBanknotes();

    void addBanknotes(int numberToAdd);

    int withdrawBanknotes(int banknotesToWithdraw);

    boolean allowWithdrawal(int wantedNumberOfBanknotes);

    Locker clone();
}
