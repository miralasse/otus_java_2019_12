package ru.otus.l11.atm;

/**
 * ClientInteractingATM.
 *
 * @author Evgeniya_Yanchenko
 */
public interface ClientInteractingATM {

    void deposit(BanknoteType banknoteType, int numberOfBanknotes);

    long withdraw(long amount);
}
