package ru.otus.model;

import lombok.Data;

import javax.persistence.*;
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

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressDataSet address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<PhoneDataSet> phones = new HashSet<>();

    public User() {
    }

    public User(long id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public User(String name, String login, String password, AddressDataSet address, Set<PhoneDataSet> phones) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.address = address;
        this.phones = phones;
    }
}
