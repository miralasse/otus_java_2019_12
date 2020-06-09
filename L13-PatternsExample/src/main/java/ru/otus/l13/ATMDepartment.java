package ru.otus.l13;

import static java.util.stream.Collectors.toList;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.otus.l13.atm.ATM;
import ru.otus.l13.atm.Countable;

import java.util.ArrayList;
import java.util.List;

/**
 * ATMDepartment.
 *
 * @author Evgeniya_Yanchenko
 */
@EqualsAndHashCode
@Getter
public class ATMDepartment implements Countable, Cloneable {

    private final List<ATM> atms;    //intentionally mutable

    public ATMDepartment() {
        this.atms = new ArrayList<>();
    }

    public ATMDepartment(List<ATM> atms) {
        this.atms = atms;
    }

    @Override
    public long getTotalAmount() {
        return atms.stream()
                .mapToLong(ATM::getTotalAmount)
                .sum();
    }

    public void restoreATMState() {
        atms.forEach(ATM::restoreState);
    }

    @Override
    public ATMDepartment clone() {
        List<ATM> copyOfATMs = atms.stream()
                .map(ATM::clone)
                .collect(toList());
        return new ATMDepartment(copyOfATMs);
    }

    public void addATM(ATM atm) {
        atms.add(atm);
    }

    public void removeATM(ATM atm) {
        atms.remove(atm);
    }
}
