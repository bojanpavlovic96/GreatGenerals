package model.component.field;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.PlayerData;
import model.component.Terrain;
import model.component.field.option.FieldOption;
import model.component.unit.Unit;
import model.event.ModelEventHandler;

public interface Field {

	boolean isVisible();

	Point2D getStoragePosition();

	void setStoragePosition(Point2D storage_position);

	PlayerData getPlayer();
	
	void setModelEventHandler(ModelEventHandler handler);
	
	// unit specific
	Unit getUnit();

	void setUnit(Unit units);

	List<Unit> getBattle();

	void addToBattle(Unit new_unit);

	Unit removeFromBattle(Unit unit);

	// done with units

	Terrain getTerrain();

	void setTerrain(Terrain terrain);

	boolean isInBattle();

	// more place in battle
	boolean haveMorePlace();

	List<FieldOption> getOptions();

	void setOptions(List<FieldOption> options);

	void addOption(FieldOption option);

	void removeOption(String option_name);

}
