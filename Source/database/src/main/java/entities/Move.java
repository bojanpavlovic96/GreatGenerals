package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "move")
public class Move {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private int prev_field_row;
	private int prev_field_col;

	private int next_field_row;
	private int next_field_col;
	
	@ManyToOne
	private Command command;

	public Move() {

	}

	public Move(int prev_field_row, int prev_field_col, int next_fiel_row, int next_field_col,
			Command command) {
		this.prev_field_row = prev_field_row;
		this.prev_field_col = prev_field_col;
		next_field_row = next_fiel_row;
		this.next_field_col = next_field_col;

		this.command = command;

	}

	public int getPrev_field_row() {
		return prev_field_row;
	}

	public void setPrev_field_row(int prev_field_row) {
		this.prev_field_row = prev_field_row;
	}

	public int getPrev_field_col() {
		return prev_field_col;
	}

	public void setPrev_field_col(int prev_field_col) {
		this.prev_field_col = prev_field_col;
	}

	public int getNext_field_row() {
		return next_field_row;
	}

	public void setNext_field_row(int next_field_row) {
		this.next_field_row = next_field_row;
	}

	public int getNext_field_col() {
		return next_field_col;
	}

	public void setNext_field_col(int next_field_col) {
		this.next_field_col = next_field_col;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
