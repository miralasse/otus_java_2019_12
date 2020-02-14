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
public class Locker {

    public static final String WRONG_NUMBER_OF_BANKNOTES = "Некорректное количество купюр";

    private final BanknoteType banknoteType;
    private int numberOfBanknotes;

    public static Locker of(BanknoteType banknoteType, int numberOfBanknotes) {
        if (numberOfBanknotes < 0) {
            throw new IllegalArgumentException(WRONG_NUMBER_OF_BANKNOTES);
        }
        return new Locker(banknoteType, numberOfBanknotes);
    }

    public void addBanknotes(int numberToAdd) {
        if (numberToAdd <= 0) {
            throw new IllegalArgumentException(WRONG_NUMBER_OF_BANKNOTES);
        }
        numberOfBanknotes += numberToAdd;
    }

    public void giveBanknotes(int numberToDelete) {
        if (numberToDelete <= 0) {
            throw new IllegalArgumentException(WRONG_NUMBER_OF_BANKNOTES);
        }
        numberOfBanknotes -= numberToDelete;
    }

    public long getAmount() {
        return (long) banknoteType.getNominalValue() * numberOfBanknotes;
    }

}
