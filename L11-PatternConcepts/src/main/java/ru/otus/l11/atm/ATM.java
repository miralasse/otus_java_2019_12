package ru.otus.l11.atm;

/**
 * Deposit.
 *
 * @author Evgeniya_Yanchenko
 */
public interface ATM {

    void deposit(BanknoteType banknoteType, int numberOfBanknotes);

    long withdraw(long amount);

    long getTotalAmount();
}
