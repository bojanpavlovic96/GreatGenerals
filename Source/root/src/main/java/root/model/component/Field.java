package root.model.component;

import java.util.List;

import javafx.geometry.Point2D;
import root.controller.Controller;
import root.model.PlayerData;
import root.model.component.option.FieldOption;
import root.model.event.ModelEventHandler;

public interface Field {

	boolean isVisible();

	Unit getUnit();

	Terrain getTerrain();

	PlayerData getPlayer();

	void setUnit(Unit new_unit);

	Point2D getStoragePosition();

	void setModelEventHandler(ModelEventHandler event_handler);

	// TODO add something about battle

	boolean isInBattle();

	List<FieldOption> getEnabledOptions();

	void initializeOptions(List<FieldOption> new_options);

	void adjustOptionsFor(Field second_field);

}
