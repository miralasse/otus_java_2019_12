package ru.otus.l13.atm;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.otus.l13.locker.Locker;

import java.util.Set;

/**
 * ATMCreator.
 *
 * @author Evgeniya_Yanchenko
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ATMCreator {

    public static ATM createATM(Set<Locker> lockerSet) {
        ATM basicATM = new ATMImpl();
        basicATM.addLockers(lockerSet);
        basicATM.saveState();
        return basicATM;
    }
}
