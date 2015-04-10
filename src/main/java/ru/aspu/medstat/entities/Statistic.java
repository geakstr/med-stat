package ru.aspu.medstat.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Statistic {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@ManyToOne
	@JoinColumn(name = "user_gym")
	private UserGym userGym;

	@Column(nullable = false)
	public double percent;

	@Column(name = "stat_date", nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date date = new Date();

	public Statistic() { }

	public UserGym getUserGym() {
		return userGym;
	}

	public void setUserGym(UserGym userGym) {
		this.userGym = userGym;
	}

	@Override
	public String toString() {
		return "Statistic [id=" + id + ", userGym=" + userGym.id + ", percent="
				+ percent + ", date=" + date + "]";
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
		Statistic other = (Statistic) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
