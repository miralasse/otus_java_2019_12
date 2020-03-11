package ru.otus.l11.atm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.l11.atm.BanknoteType.TWO_THOUSAND;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * LockerImplTest.
 *
 * @author Evgeniya_Yanchenko
 */
class LockerImplTest {

    private static final int STANDARD_NUMBER_OF_BANKNOTES = 50;
    private static final BanknoteType BANKNOTE_TYPE = TWO_THOUSAND;
    private LockerImpl locker;

    @BeforeEach
    void setUp() {
        locker = LockerImpl.of(BANKNOTE_TYPE, STANDARD_NUMBER_OF_BANKNOTES);
    }

    @Test
    void testAddBanknotes() {
        int numberOfBanknotesBefore = locker.getNumberOfBanknotes();
        int numberToAdd = 5;
        locker.addBanknotes(numberToAdd);
        int numberOfBanknotesAfter = locker.getNumberOfBanknotes();
        assertThat(numberOfBanknotesAfter).isEqualTo(numberOfBanknotesBefore + numberToAdd);
    }

    @Test
    void testSuccessfulWithdrawBanknotes() {
        int numberOfBanknotesBefore = locker.getNumberOfBanknotes();
        int numberToWithdraw = 3;
        locker.withdrawBanknotes(numberToWithdraw);
        int numberOfBanknotesAfter = locker.getNumberOfBanknotes();
        assertThat(numberOfBanknotesAfter).isEqualTo(numberOfBanknotesBefore - numberToWithdraw);
    }

    @Test
    void testFailedWithdrawBanknotes() {
        int numberOfBanknotesBefore = locker.getNumberOfBanknotes();
        int numberToWithdraw = numberOfBanknotesBefore + 1;
        int result = locker.withdrawBanknotes(numberToWithdraw);
        int numberOfBanknotesAfter = locker.getNumberOfBanknotes();
        assertThat(numberOfBanknotesAfter).isEqualTo(numberOfBanknotesBefore);
        assertThat(result).isEqualTo(-1);
    }

    @Test
    void testGetAmount() {
        long expectedSum = (long) BANKNOTE_TYPE.getNominalValue() * STANDARD_NUMBER_OF_BANKNOTES;
        long actualSum = locker.getAmount();
        assertThat(actualSum).isEqualTo(expectedSum);
    }

    @Test
    void testAllowWithdrawal() {
        int allowedNumber = STANDARD_NUMBER_OF_BANKNOTES - 1;
        assertTrue(locker.allowWithdrawal(allowedNumber));

        int forbiddenNumber = STANDARD_NUMBER_OF_BANKNOTES + 1;
        assertFalse(locker.allowWithdrawal(forbiddenNumber));
    }
}