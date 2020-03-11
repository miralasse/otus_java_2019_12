package ru.otus.l11.atm;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.l11.atm.ATMImpl.LIMIT_ERROR;
import static ru.otus.l11.atm.BanknoteType.FIFTY;
import static ru.otus.l11.atm.BanknoteType.FIVE_HUNDRED;
import static ru.otus.l11.atm.BanknoteType.FIVE_THOUSAND;
import static ru.otus.l11.atm.BanknoteType.ONE_HUNDRED;
import static ru.otus.l11.atm.BanknoteType.ONE_THOUSAND;
import static ru.otus.l11.atm.BanknoteType.TWO_HUNDRED;
import static ru.otus.l11.atm.BanknoteType.TWO_THOUSAND;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * ATMTest.
 *
 * @author Evgeniya_Yanchenko
 */
class ATMImplTest {

    private static final int STANDARD_NUMBER_OF_BANKNOTES = 100;
    private ATMImpl atm;
    private Set<Locker> lockerSet;

    @BeforeEach
    void setUp() {
        atm = new ATMImpl();
        lockerSet = Set.of(
                LockerImpl.of(FIFTY, STANDARD_NUMBER_OF_BANKNOTES),
                LockerImpl.of(ONE_HUNDRED, STANDARD_NUMBER_OF_BANKNOTES),
                LockerImpl.of(TWO_HUNDRED, STANDARD_NUMBER_OF_BANKNOTES),
                LockerImpl.of(FIVE_HUNDRED, STANDARD_NUMBER_OF_BANKNOTES),
                LockerImpl.of(ONE_THOUSAND, STANDARD_NUMBER_OF_BANKNOTES),
                LockerImpl.of(TWO_THOUSAND, STANDARD_NUMBER_OF_BANKNOTES),
                LockerImpl.of(FIVE_THOUSAND, STANDARD_NUMBER_OF_BANKNOTES)
        );
        atm.addLockers(lockerSet);
    }

    @Test
    void testGetTotalAmount() {
        long expectedSum = lockerSet.stream()
                .mapToLong(l -> l.getBanknoteType().getNominalValue() * l.getNumberOfBanknotes())
                .sum();
        long actualSum = atm.getTotalAmount();
        assertThat(actualSum).isEqualTo(expectedSum);
    }

    @Test
    void testSimpleDeposit() {
        long sumToDeposit = 3000;
        long expectedSum = atm.getTotalAmount() + sumToDeposit;

        atm.deposit(ONE_THOUSAND, 3);

        long actualSum = atm.getTotalAmount();
        assertThat(actualSum).isEqualTo(expectedSum);
    }

    @Test
    void testDepositDifferentNominalValues() {
        long sumToDeposit = 8850;
        long expectedSum = atm.getTotalAmount() + sumToDeposit;

        for (BanknoteType banknoteType : BanknoteType.values()) {
            atm.deposit(banknoteType, 1);
        }

        long actualSum = atm.getTotalAmount();
        assertThat(actualSum).isEqualTo(expectedSum);
    }

    @Test
    void testSuccessfulWithdrawal() {
        long sumToWithdraw = 7000;
        long expectedSum = atm.getTotalAmount() - sumToWithdraw;

        atm.withdraw(sumToWithdraw);

        long actualSum = atm.getTotalAmount();
        assertThat(actualSum).isEqualTo(expectedSum);
    }

    @Test()
    void testFailedWithdrawal() {
        long sumToWithdraw = 320;
        ATMException limitException =
                assertThrows(ATMException.class,
                        () -> atm.withdraw(sumToWithdraw),
                        "Expected to throw limit error, but it didn't");
        assertThat(limitException.getMessage()).isEqualTo(LIMIT_ERROR);
    }


}