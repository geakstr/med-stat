package ru.aspu.medstat.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users_gyms")
public class UserGym {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
	@ManyToOne
	@JoinColumn(name = "gymnastic_id")
    private Gymnastic gymnastic;
    
    @Column(nullable = false)
    public int complete = 0;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userGym")
    public List<Statistic> stats;
    
    public UserGym() {
    	this.stats = new ArrayList<>();
    }

    public void addStat(Statistic stat) {
    	this.stats.add(stat);
    	stat.setUserGym(this);
    }
    
	public Gymnastic getGymnastic() {
		return gymnastic;
	}
	public void setGymnastic(Gymnastic gymnastic) {
		this.gymnastic = gymnastic;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "UserGym [id=" + id + ", gymnastic=" + gymnastic.id + ", user="
				+ user.id + ", complete=" + complete + "]";
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
		UserGym other = (UserGym) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
