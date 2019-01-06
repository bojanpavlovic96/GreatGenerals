package model.component;

import java.util.List;

import javafx.geometry.Point2D;
import model.component.unit.Unit;

public interface Field {

	Point2D getStoragePosition();

	void setStoragePosition(Point2D storage_position);

	List<Unit> getUnits();

	void setUnits(List<Unit> units);

	Unit removeUnit(String unit_id);

	void addUnit(Unit new_unit);

	Terrain getTerrain();

	void setTerrain(Terrain terrain);

	void moveToField(Field second_field);

	boolean isInBattle();

	// more place in battle
	boolean haveMorePlace();

	// moving path

}
