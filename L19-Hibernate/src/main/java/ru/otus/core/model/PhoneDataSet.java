package ru.otus.core.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * AddressDataSet.
 *
 * @author Evgeniya_Yanchenko
 */
@Data
@Entity
@Table(name = "phones")
public class PhoneDataSet {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number, User user) {
        this.number = number;
        this.user = user;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", user=" + user.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDataSet)) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return id != null &&
                Objects.equals(id, that.id) &&
                Objects.equals(number, that.number) &&
                Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, user.getId());
    }
}
