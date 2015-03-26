package ru.aspu.medstat.entities;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(unique = true)
    public String email;

    @Column(nullable = true)
    public String firstName;

    @Column(nullable = true)
    public String lastName;

    @Column(nullable = true)
    public String birthDate;

    @Column(nullable = true)
    public String password;

    @Column(nullable = false)
    public int role = UserRoles.PATIENT;

    @Column(nullable = true)
    public String phone;

    @Column(nullable = true)
    public String emailToken;

    @Column(nullable = false)
    public boolean emailApproved = false;

    @Column(nullable = false)
    public boolean wasLogin = false;

    public User() {}

    @Override
    public String toString() {
        return String.format(
                "User [id=%d, email='%s', firstName='%s', lastName='%s', bd='%s', role='%s']",
                id, email, firstName, lastName, birthDate, role);
    }
}
