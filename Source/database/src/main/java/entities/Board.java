package entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "board")
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToMany
	private List<Territory> territories;

	private int territory_num;

	public Board() {

	}

	public Board(long id, List<Territory> territories, int territory_num) {
		super();
		this.id = id;
		this.territories = territories;
		this.territory_num = territory_num;
	}

	public int getTerritory_num() {
		return territory_num;
	}

	public void setTerritory_num(int territory_num) {
		this.territory_num = territory_num;
	}

}
