package ru.otus.l11.atm;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Locker.
 *
 * @author Evgeniya_Yanchenko
 */

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LockerImpl implements Locker{

    public static final String WRONG_NUMBER_OF_BANKNOTES = "Некорректное количество купюр";

    private final BanknoteType banknoteType;
    private int numberOfBanknotes;

    public static LockerImpl of(BanknoteType banknoteType, int numberOfBanknotes) {
        if (numberOfBanknotes < 0) {
            throw new IllegalArgumentException(WRONG_NUMBER_OF_BANKNOTES);
        }
        return new LockerImpl(banknoteType, numberOfBanknotes);
    }

    @Override
    public void addBanknotes(int numberToAdd) {
        if (numberToAdd <= 0) {
            throw new IllegalArgumentException(WRONG_NUMBER_OF_BANKNOTES);
        }
        numberOfBanknotes += numberToAdd;
    }

    @Override
    public int withdrawBanknotes(int banknotesToWithdraw) {
        if (banknotesToWithdraw <= 0) {
            throw new IllegalArgumentException(WRONG_NUMBER_OF_BANKNOTES);
        }
        if (allowWithdrawal(banknotesToWithdraw)) {
            numberOfBanknotes -= banknotesToWithdraw;
            return banknotesToWithdraw;
        } else {
            return -1;
        }
    }

    @Override
    public long getAmount() {
        return (long) banknoteType.getNominalValue() * numberOfBanknotes;
    }

    @Override
    public boolean allowWithdrawal(int wantedNumberOfBanknotes) {
        return wantedNumberOfBanknotes <= numberOfBanknotes;
    }

}
