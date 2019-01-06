package model.component;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import model.component.unit.Unit;

public class GameField implements Field {

	private Point2D storage_position;

	private Unit unit;
	private Terrain terrain;

	private List<Unit> units_in_battle;

	// methods

	public GameField(Point2D storage_position, Unit unit, Terrain terrain) {
		super();

		this.storage_position = storage_position;
		this.unit = unit;
		this.terrain = terrain;

		this.units_in_battle = new ArrayList<Unit>();
	}

	public Point2D getStoragePosition() {

		return storage_position;
	}

	public void setStoragePosition(Point2D storage_position) {
		this.storage_position = storage_position;
	}

	public Unit getUnit() {
		return this.unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public void moveToField(Field second_field) {

		second_field.setUnit(this.unit);

		// update units references to field
		this.unit.getMoveType().setMy_field(second_field);

		this.unit = null;
		// for now that's all

	}

	// battle specific

	public boolean isInBattle() {
		return this.units_in_battle.size() > 1;
	}

	public boolean haveMorePlace() {
		return this.units_in_battle.size() < 6;
	}

	public List<Unit> getBattle() {
		return this.units_in_battle;
	}

	public void addToBattle(Unit new_unit) {
		this.units_in_battle.add(new_unit);
	}

	public Unit removeFromBattle(Unit unit) {
		int index = 0;
		for (Unit b_unit : this.units_in_battle) {
			if (b_unit.equals(unit)) {

				this.units_in_battle.remove(index);
				return b_unit;
			}

			index++;
		}

		return null;
	}

}
