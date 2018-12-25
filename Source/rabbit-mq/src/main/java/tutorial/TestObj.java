package tutorial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "test_table")
public class TestObj {

	@Id
	@GeneratedValue
	private int id;

	private String name;

	@ManyToOne
	private SecondObject second_ref;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SecondObject getSecond_ref() {
		return second_ref;
	}

	public void setSecond_ref(SecondObject second_ref) {
		this.second_ref = second_ref;
	}

}
