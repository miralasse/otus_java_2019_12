package ru.otus.core.model;


import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressDataSet address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<PhoneDataSet> phones = new HashSet<>();

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, AddressDataSet address, Set<PhoneDataSet> phones) {
        this.name = name;
        this.address = address;
        this.phones = phones;
    }
}
