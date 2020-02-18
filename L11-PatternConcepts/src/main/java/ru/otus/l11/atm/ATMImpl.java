package ru.otus.l11.atm;

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
public class ATMImpl implements ATM {

    public static final String LIMIT_ERROR = "Запрашиваемая сумма не может быть выдана";
    public static final String LOCKER_ALREADY_EXISTS_ERROR = "Ячейка с таким номиналом купюр уже есть в банкомате";
    public static final String LOCKER_DOES_NOT_EXISTS_ERROR = "Ячейки для такого номинала купюр нет в банкомате";

    public static final String LOCKER_SHOULD_NOT_BE_NULL = "Ячейка(-и) не указана";
    public static final String BANKNOTE_TYPE_SHOULD_NOT_BE_NULL = "Номинал не указан";

    private final Map<BanknoteType, Locker> lockers = new TreeMap<>((l1, l2) -> l2.getNominalValue() - l1.getNominalValue());

    public void addLocker(Locker locker) {
        if (locker == null) {
            throw new ATMException(LOCKER_SHOULD_NOT_BE_NULL);
        }
        Locker existingLocker = lockers.putIfAbsent(locker.getBanknoteType(), locker);
        if (existingLocker != null) {
            throw new ATMException(LOCKER_ALREADY_EXISTS_ERROR);
        }
    }

    public void addLockers(Set<Locker> lockerSet) {
        if (lockerSet == null) {
            throw new ATMException(LOCKER_SHOULD_NOT_BE_NULL);
        }
        lockerSet.forEach(this::addLocker);
    }

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
                .mapToLong(Locker::getAmount)
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

}
