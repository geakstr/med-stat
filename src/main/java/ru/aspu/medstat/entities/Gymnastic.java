package ru.aspu.medstat.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Gymnastic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(nullable = false)
    public String title;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gymnastic")
    public List<Statistic> statistics;

    public Gymnastic() {
        this.statistics = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format(
                "Gymnastic [id=%d, title=%s]",
                this.id, this.title);
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
        Gymnastic other = (Gymnastic) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
