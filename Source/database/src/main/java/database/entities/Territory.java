package database.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "territory")
public class Territory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	public Territory() {
	}

	public Territory(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// algorithm for territory creation

}
