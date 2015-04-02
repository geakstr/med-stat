package ru.aspu.medstat.entities;

import org.springframework.format.annotation.DateTimeFormat;
import ru.aspu.medstat.utils.PasswordCrypto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(name = "email", unique = true, nullable = false)
    public String email;

    @Column(name = "first_name", nullable = true)
    public String firstName;

    @Column(name = "last_name", nullable = true)
    public String lastName;

    @Column(name = "birth_date", nullable = true)
    public String birthDate;

    @Column(name = "password", nullable = true)
    public String password;

    @Column(name = "role", nullable = false)
    public int role = User.Roles.PATIENT.getValue();

    @Column(name = "phone", nullable = true)
    public String phone;

    @Column(name = "email_token", nullable = false)
    public String emailToken;

    @Column(name = "email_approved", nullable = false)
    public boolean emailApproved = false;

    @Column(name = "was_login", nullable = false)
    public boolean wasLogin = false;

    @Column(nullable = false)
    public long doctorId = -1;

    @Column(name = "reg_date", nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    public Date registrationDate = new Date();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    public List<Statistic> statistics;

    public User() {
        this.statistics = new ArrayList<>();
    }

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public static enum Roles {
        ADMIN(0, "ROLE_ADMIN"), PATIENT(1, "ROLE_PATIENT"), DOCTOR(2, "ROLE_DOCTOR");

        private int value;
        private String name;

        private Roles(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
}
