package model.component;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;

public class GameField implements Field {

	private Point2D storage_position;

	private List<Unit> units;
	private Terrain terrain;

	public GameField(Point2D storage_position, Unit unit, Terrain terrain) {
		super();

		this.units = new ArrayList<Unit>();

		this.storage_position = storage_position;
		if (unit != null)
			this.units.add(unit);
		this.terrain = terrain;
	}

	// public GameField(Point2D storage_position, List<Unit> units, Terrain terrain)
	// {
	// super();
	//
	// this.storage_position = storage_position;
	// this.units = units;
	// this.terrain = terrain;
	// }

	public Point2D getStoragePosition() {
		return storage_position;
	}

	public void setStoragePosition(Point2D storage_position) {
		this.storage_position = storage_position;
	}

	public List<Unit> getUnits() {
		return this.units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public void moveToField(Field second_field) {

		second_field.setUnits(new ArrayList<Unit>(this.units));
		this.units.clear();
		// for now that's all

	}

	public Unit removeUnit(String unit_id) {

		for (Unit unit : this.units) {
			if (unit.getUnitId().equals(unit_id))
				return unit;
		}

		return null;
	}

	public void addUnit(Unit new_unit) {
		this.units.add(new_unit);
	}

	public boolean isInBattle() {
		return this.units.size() > 1;
		// if there is more than one unit on the same field ... it's battle
	}

	public boolean haveMorePlace() {
		return this.units.size() < 6;
	}

}