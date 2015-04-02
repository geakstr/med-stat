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
    private Gymnastic gymnastic;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    public double percent;

    @Column(name = "stat_date", nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    public Date date = new Date();

    public Statistic() {}

    @Override
    public String toString() {
        return String.format(
                "Stat [id=%d, gymnastic_id=%s, user_id=%d, percent=%.2f, date=%s]",
                this.id, this.gymnastic.title, this.user.id, this.percent, this.date);
    }
    
    

    public Gymnastic getGymnastic() {
		return gymnastic;
	}
	public void setGymnastic(Gymnastic gymnastic) {
		gymnastic.statistics.add(this);
		this.gymnastic = gymnastic;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		user.statistics.add(this);
		this.user = user;
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
