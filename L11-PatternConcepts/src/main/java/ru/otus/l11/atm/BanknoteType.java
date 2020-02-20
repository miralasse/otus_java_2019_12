package ru.otus.l11.atm;

import lombok.Getter;

/**
 * BanknoteValues.
 *
 * @author Evgeniya_Yanchenko
 */
@Getter
public enum BanknoteType {

    FIFTY(50), ONE_HUNDRED(100), TWO_HUNDRED(200), FIVE_HUNDRED(500),
    ONE_THOUSAND(1000), TWO_THOUSAND(2000), FIVE_THOUSAND(5000);

    private final int nominalValue;

    BanknoteType(int nominalValue) {
        this.nominalValue = nominalValue;
    }
}
