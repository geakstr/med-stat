package ru.aspu.medstat.entities;

import ru.aspu.medstat.utils.PasswordCrypto;
import ru.aspu.medstat.utils.PasswordUtils;

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

    public static User create(String email, String password) {
        User user = new User();
        user.email = email;
        user.password = PasswordCrypto.getInstance().encrypt(password);
        return user;
    }

    @Override
    public String toString() {
        return String.format(
                "User [id=%d, email='%s', firstName='%s', lastName='%s', bd='%s', role='%s']",
                id, email, firstName, lastName, birthDate, role);
    }
}
