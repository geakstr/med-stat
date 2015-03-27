package ru.aspu.medstat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    
    @Column(nullable = false)
    public long doctorId = -1;
    
    @Column(nullable = false)
    public String registrationDate;

    public User() {}

    @Override
    public String toString() {
        return String.format(
                "User [id=%d, email='%s', firstName='%s', lastName='%s', bd='%s', role='%s', doctor='%d', registrationDate='%s']",
                id, email, firstName, lastName, birthDate, role, doctorId, registrationDate);
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
}
