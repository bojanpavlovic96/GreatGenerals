package root.model.component;

import javafx.geometry.Point2D;
import root.model.PlayerData;
import root.model.event.ModelEventHandler;

public interface Field {

	boolean isVisible();

	Point2D getStoragePosition();

	// maybe setStoragePosition

	PlayerData getPlayer();

	void setModelEventHandler(ModelEventHandler event_handler);

	Unit getUnit();

	void setUnit(Unit new_unit);

	// TODO add something about battle

	boolean isInBattle();

	Terrain getTerrain();

	// no need for setTerrain, it is set from the constructor and never changed

	// TODO something about options

}
