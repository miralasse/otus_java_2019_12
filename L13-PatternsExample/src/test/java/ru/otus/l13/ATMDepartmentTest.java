package ru.otus.l13;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.l13.BanknoteType.FIFTY;
import static ru.otus.l13.BanknoteType.FIVE_HUNDRED;
import static ru.otus.l13.BanknoteType.FIVE_THOUSAND;
import static ru.otus.l13.BanknoteType.ONE_HUNDRED;
import static ru.otus.l13.BanknoteType.ONE_THOUSAND;
import static ru.otus.l13.BanknoteType.TWO_HUNDRED;
import static ru.otus.l13.BanknoteType.TWO_THOUSAND;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.l13.atm.ATM;
import ru.otus.l13.atm.ATMCreator;
import ru.otus.l13.atm.Countable;
import ru.otus.l13.locker.Locker;
import ru.otus.l13.locker.LockerImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * ATMDepartmentTest.
 *
 * @author Evgeniya_Yanchenko
 */
public class ATMDepartmentTest {

    private static final int FIRST_ATM_NUMBER_OF_BANKNOTES = 100;
    private static final int SECOND_ATM_NUMBER_OF_BANKNOTES = 50;
    private static final int THIRD_ATM_NUMBER_OF_BANKNOTES = 80;

    private ATMDepartment department;
    private ATM firstATM;
    private ATM secondATM;
    private ATM thirdATM;


    @BeforeEach
    void setUp() {
        Set<Locker> firstLockerSet = Set.of(
                LockerImpl.of(FIFTY, FIRST_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(ONE_HUNDRED, FIRST_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(TWO_HUNDRED, FIRST_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(FIVE_HUNDRED, FIRST_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(ONE_THOUSAND, FIRST_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(TWO_THOUSAND, FIRST_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(FIVE_THOUSAND, FIRST_ATM_NUMBER_OF_BANKNOTES)
        );
        Set<Locker> secondLockerSet = Set.of(
                LockerImpl.of(FIFTY, SECOND_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(ONE_HUNDRED, SECOND_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(TWO_HUNDRED, SECOND_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(FIVE_HUNDRED, SECOND_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(ONE_THOUSAND, SECOND_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(TWO_THOUSAND, SECOND_ATM_NUMBER_OF_BANKNOTES)
        );
        Set<Locker> thirdLockerSet = Set.of(
                LockerImpl.of(ONE_HUNDRED, THIRD_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(FIVE_HUNDRED, THIRD_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(ONE_THOUSAND, THIRD_ATM_NUMBER_OF_BANKNOTES),
                LockerImpl.of(FIVE_THOUSAND, THIRD_ATM_NUMBER_OF_BANKNOTES)
        );
        firstATM = ATMCreator.createATM(firstLockerSet);
        secondATM = ATMCreator.createATM(secondLockerSet);
        thirdATM = ATMCreator.createATM(thirdLockerSet);

        department = new ATMDepartment(List.of(firstATM, secondATM, thirdATM));
    }

    @Test
    void testGetTotalAmount() {
        long expectedSum = Stream.of(firstATM, secondATM, thirdATM)
                .mapToLong(Countable::getTotalAmount)
                .sum();
        long actualSum = department.getTotalAmount();
        assertThat(actualSum).isEqualTo(expectedSum);
    }

    @Test
    void testSaveAndRestoreATMs() {
        ATMDepartment clone = new ATMDepartment(department.getAtms()
                .stream()
                .map(ATM::clone)
                .collect(toList()));
        assertThat(clone).isEqualTo(department);

        firstATM.saveState();
        firstATM.deposit(FIVE_THOUSAND, 5);
        assertThat(firstATM).isNotEqualTo(clone.getAtms().get(0));

        secondATM.saveState();
        secondATM.withdraw(1500);
        assertThat(secondATM).isNotEqualTo(clone.getAtms().get(1));

        assertThat(clone).isNotEqualTo(department);

        department.restoreATMState();
        assertThat(clone).isEqualTo(department);
    }


}
