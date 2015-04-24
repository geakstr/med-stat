package ru.aspu.medstat.entities;

import java.util.Date;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "messages")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	@ManyToOne
	@JoinColumn(name = "from_user")
	public User fromUser;
	@ManyToOne
	@JoinColumn(name = "to_user")
	public User toUser;
	@Column(name = "date", nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date date = new Date();
	@Column(nullable = true)
	public String header;
	@Column(nullable = false)
	public String message;
	@Column(name="is_read", nullable = false)
	public int isRead = 0;
	@Column(name="delete_to", nullable = false)
	public int deleteTo;
	@Column(name="delete_from",nullable = false)
	public int deleteFrom;

	public Message() { }

	@Override
	public String toString() {
		return "Message [id=" + id + ", fromUser=" + fromUser + ", toUser="
				+ toUser + ", date=" + date + ", header=" + header
				+ ", message=" + message + ", isRead=" + isRead + ", deleteTo="
				+ deleteTo + ", deleteFrom=" + deleteFrom + "]";
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
		Message other = (Message) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
