package database.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chat_message")
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String text;

	private long message_index;

	private Date date_time;

	@ManyToOne
	private Player sender;

	public ChatMessage() {

	}

	public ChatMessage(long id, String text, long index, Date date_time, Player sender) {
		super();
		this.id = id;
		this.text = text;
		this.message_index = index;
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

	public long getMessage_index() {
		return message_index;
	}

	public void setMessage_index(long index) {
		this.message_index = index;
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
