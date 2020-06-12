package ru.otus.core.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AddressDataSet.
 *
 * @author Evgeniya_Yanchenko
 */
@Data
@Entity
@Table(name = "addresses")
public class AddressDataSet {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    public AddressDataSet() {
    }

    public AddressDataSet(String street) {
        this.street = street;
    }
}
