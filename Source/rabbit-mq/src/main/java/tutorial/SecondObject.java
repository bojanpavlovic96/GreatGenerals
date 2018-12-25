package tutorial;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "second_table")
public class SecondObject {

	@Id
	@GeneratedValue
	private int id;

	@OneToMany(mappedBy = "second_ref")
	private List<TestObj> test_obj;

	@Column
	private String value1;

	@Column
	private String value2;

	@Column
	private int value3;

	public SecondObject() {
		this.test_obj = new ArrayList<TestObj>();
	}

	public SecondObject(String value1, String value2, int value3) {

		this.test_obj = new ArrayList<TestObj>();

		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public int getValue3() {
		return value3;
	}

	public void setValue3(int value3) {
		this.value3 = value3;
	}

	public List<TestObj> getTest_obj() {
		return test_obj;
	}

	public void setTest_obj(List<TestObj> test_obj) {
		this.test_obj = test_obj;
	}

}
