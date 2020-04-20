package ru.otus.l13.atm;

import static java.util.stream.Collectors.toSet;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.otus.l13.ATMException;
import ru.otus.l13.BanknoteType;
import ru.otus.l13.locker.Locker;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * ATM.
 *
 * @author Evgeniya_Yanchenko
 */
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ATMImpl implements ATM {

    public static final String LIMIT_ERROR = "Запрашиваемая сумма не может быть выдана";
    public static final String LOCKER_ALREADY_EXISTS_ERROR = "Ячейка с таким номиналом купюр уже есть в банкомате";
    public static final String LOCKER_DOES_NOT_EXISTS_ERROR = "Ячейки для такого номинала купюр нет в банкомате";

    public static final String LOCKER_SHOULD_NOT_BE_NULL = "Ячейка(-и) не указана";
    public static final String BANKNOTE_TYPE_SHOULD_NOT_BE_NULL = "Номинал не указан";

    private Map<BanknoteType, Locker> lockers = new TreeMap<>((l1, l2) -> l2.getNominalValue() - l1.getNominalValue());
    private MementoATM savedState;

    @Override
    public void addLocker(Locker locker) {
        if (locker == null) {
            throw new ATMException(LOCKER_SHOULD_NOT_BE_NULL);
        }
        Locker existingLocker = lockers.putIfAbsent(locker.getBanknoteType(), locker);
        if (existingLocker != null) {
            throw new ATMException(LOCKER_ALREADY_EXISTS_ERROR);
        }
    }

    @Override
    public void addLockers(Set<Locker> lockerSet) {
        if (lockerSet == null) {
            throw new ATMException(LOCKER_SHOULD_NOT_BE_NULL);
        }
        lockerSet.forEach(this::addLocker);
    }

    @Override
    public void removeLocker(Locker locker) {
        if (locker == null) {
            throw new ATMException(LOCKER_SHOULD_NOT_BE_NULL);
        }
        lockers.remove(locker.getBanknoteType());
    }

    @Override
    public void deposit(BanknoteType banknoteType, int numberOfBanknotes) {
        if (banknoteType == null) {
            throw new ATMException(BANKNOTE_TYPE_SHOULD_NOT_BE_NULL);
        }
        Locker locker = lockers.get(banknoteType);
        if (locker == null) {
            throw new ATMException(LOCKER_DOES_NOT_EXISTS_ERROR);
        }
        locker.addBanknotes(numberOfBanknotes);
    }

    @Override
    public long withdraw(long amount) {
        if (amount <= 0 || amount > getTotalAmount()) {
            throw new ATMException(LIMIT_ERROR);
        }
        return calculateWithdrawal(amount);
    }

    @Override
    public long getTotalAmount() {
        return lockers.values().stream()
                .filter(Objects::nonNull)
                .mapToLong(Locker::getTotalAmount)
                .sum();
    }

    private long calculateWithdrawal(long amount) {
        long remainingSum = amount;
        Map<Locker, Integer> withdrawalTransactionMap = new LinkedHashMap<>();

        for (Locker locker : lockers.values()) {
            int nominal = locker.getBanknoteType().getNominalValue();
            if (remainingSum > nominal) {
                int banknotesToWithdraw = (int) remainingSum / nominal;

                if (locker.allowWithdrawal(banknotesToWithdraw)) {
                    withdrawalTransactionMap.put(locker, banknotesToWithdraw);
                    remainingSum -= banknotesToWithdraw * nominal;
                    if (remainingSum == 0) {
                        return processWithdrawal(withdrawalTransactionMap);
                    }
                }
            }
        }
        throw new ATMException(LIMIT_ERROR);
    }

    private long processWithdrawal(Map<Locker, Integer> withdrawalTransactionMap) {
        long amount = 0L;
        for (Map.Entry<Locker, Integer> transaction : withdrawalTransactionMap.entrySet()) {
            Locker locker = transaction.getKey();
            int quantity = transaction.getValue();
            int numberOfBanknotes = locker.withdrawBanknotes(quantity);
            amount += numberOfBanknotes * locker.getBanknoteType().getNominalValue();
        }
        return amount;
    }


    private Map<BanknoteType, Locker> copyMapOfLockers(Map<BanknoteType, Locker> lockers) {
        Set<Locker> copyOfLockerSet = lockers.values()
                .stream()
                .map(Locker::clone)
                .collect(toSet());

        Map<BanknoteType, Locker> copyOfLockers = new TreeMap<>((l1, l2) -> l2.getNominalValue() - l1.getNominalValue());
        copyOfLockerSet.forEach(locker -> copyOfLockers.put(locker.getBanknoteType(), locker));
        return copyOfLockers;
    }

    @Override
    public ATMImpl clone() {
        ATMImpl clone = new ATMImpl();
        clone.lockers = copyMapOfLockers(lockers);

        Map<BanknoteType, Locker> copyOfMementoLockers = copyMapOfLockers(savedState.mementoLockers);
        clone.savedState = new MementoATM(copyOfMementoLockers);

        return clone;
    }

    @Override
    public void saveState() {
        savedState = new MementoATM(copyMapOfLockers(lockers));
    }

    @Override
    public void restoreState() {
        this.lockers = savedState.mementoLockers;
    }


    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor
    static class MementoATM {
        private final Map<BanknoteType, Locker> mementoLockers;
    }
}
