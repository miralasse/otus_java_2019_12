package ru.otus.l13.atm;

import ru.otus.l13.BanknoteType;

/**
 * ClientInteractingATM.
 *
 * @author Evgeniya_Yanchenko
 */
public interface ClientInteractingATM {

    void deposit(BanknoteType banknoteType, int numberOfBanknotes);

    long withdraw(long amount);
}
