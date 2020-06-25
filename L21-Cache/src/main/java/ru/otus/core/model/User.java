package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.otus.core.annotation.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class User {

    @Id
    private long id;
    private String name;
    private int age;
}
