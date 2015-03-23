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

    @Column(nullable = true)
    public String password;

    @Column(nullable = false)
    public int role = UserRoles.PATIENT;

    @Column(nullable = true)
    public String telephone;

    @Column(nullable = true)
    public String emailToken;

    @Column
    public boolean emailApproved = false;

    public User() {}

    @Override
    public String toString() {
        return String.format(
                "User [id=%d, email='%s', firstName='%s', lastName='%s', bd='%s', role='%s']",
                id, email, firstName, lastName, birthDate, role);
    }
}
