package ru.otus.l11.atm;

/**
 * Locker.
 *
 * @author Evgeniya_Yanchenko
 */
public interface Locker {

    BanknoteType getBanknoteType();

    int getNumberOfBanknotes();

    void addBanknotes(int numberToAdd);

    int withdrawBanknotes(int banknotesToWithdraw);

    long getAmount();

    boolean allowWithdrawal(int wantedNumberOfBanknotes);


}
