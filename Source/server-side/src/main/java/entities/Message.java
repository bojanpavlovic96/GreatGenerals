package entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String text;

	private long index;

	private Date date_time;

	@ManyToOne
	private Player sender;

	public Message() {

	}

	public Message(long id, String text, long index, Date date_time, Player sender) {
		super();
		this.id = id;
		this.text = text;
		this.index = index;
		this.date_time = date_time;
		this.sender = sender;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public Player getSender() {
		return sender;
	}

	public void setSender(Player sender) {
		this.sender = sender;
	}

}
