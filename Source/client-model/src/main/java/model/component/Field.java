package model.component;

import java.util.List;

import javafx.geometry.Point2D;
import model.component.unit.Unit;

public interface Field {

	Point2D getStoragePosition();

	void setStoragePosition(Point2D storage_position);

	// unit specific
	Unit getUnit();

	void setUnit(Unit units);

	List<Unit> getBattle();

	void addToBattle(Unit new_unit);

	Unit removeFromBattle(Unit unit);

	// done with units

	Terrain getTerrain();

	void setTerrain(Terrain terrain);

	void moveToField(Field second_field);

	boolean isInBattle();

	// more place in battle
	boolean haveMorePlace();

}
