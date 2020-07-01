package ru.otus.model;

import lombok.Data;

import javax.persistence.*;

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
