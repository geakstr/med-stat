package ru.aspu.medstat.entities;

import ru.aspu.medstat.utils.PasswordCrypto;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(unique = true, nullable = false)
    public String email;

    @Column(name = "first_name", nullable = false)
    public String firstName;

    @Column(name = "last_name", nullable = false)
    public String lastName;

    @Column(name = "birth_date", nullable = false)
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
