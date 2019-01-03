package model;

import javafx.geometry.Point2D;

public class Field {

	private Point2D storage_position;

	private Unit unit;
	private Terrain terrain;

	public Field(Point2D storage_position, Unit unit, Terrain terrain) {
		super();
		this.storage_position = storage_position;
		this.unit = unit;
		this.terrain = terrain;
	}

	public void addSomething() {

	}

	public Point2D getStorage_position() {
		return storage_position;
	}

	public void setStorage_position(Point2D storage_position) {
		this.storage_position = storage_position;
	}

	public Unit getUnit() {
		return unit;
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

}
