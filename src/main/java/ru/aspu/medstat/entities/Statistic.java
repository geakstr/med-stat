package ru.aspu.medstat.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Statistic {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
	
	@ManyToOne
	public Gymnastic gymnastic;
	
	@ManyToOne
	public User user;
	
	@Column(nullable = false)
	public double percent;
	
    @Column(name = "stat_date", nullable = false)
    @DateTimeFormat (pattern="dd/MM/yyyy")
    public Date date = new Date();
	
	public Statistic() {}
	
	public void setGymnastic(Gymnastic gym) {
		this.gymnastic = gym;
	}
	
	@Override
	public String toString() {
		return String.format(
				"Stat [id=%d, gymnastic_id=%s, user_id=%d, percent=%.2f, date=%s]",
				this.id, this.gymnastic.title, this.user.id, this.percent, this.date);
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
