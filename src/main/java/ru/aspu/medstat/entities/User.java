package ru.aspu.medstat.entities;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(unique = true)
    public String email;

    @Column(nullable = false)
    public String firstName;

    @Column(nullable = false)
    public String lastName;

    @Column(nullable = false)
    public String birthDate;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
    public int role = UserRoles.PATIENT;

    @Column(nullable = true)
    public String telephone;

    @Column(nullable = true)
    public String emailToken;

    @Column
    public boolean emailApproved = false;

    @Column
    public boolean changedPassword = false;

    public User() {}

    public User(String email, String firstName, String lastName, String birthDate, String password, int role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.password = password;
        this.role = role;
    }

    public User(String email, String firstName, String lastName, String birthDate, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.password = password;
    }

    public User(String email, String firstName, String lastName, String birthDate, String password, String telephone) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.password = password;
        this.telephone = telephone;
    }

    public User(String email, String firstName, String lastName, String birthDate, String password, String telephone, int role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.password = password;
        this.telephone = telephone;
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format(
                "User [id=%d, email='%s', firstName='%s', lastName='%s', bd='%s', role='%s']",
                id, email, firstName, lastName, birthDate, role);
    }
}
