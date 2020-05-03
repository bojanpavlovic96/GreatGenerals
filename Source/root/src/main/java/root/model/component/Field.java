package root.model.component;

import java.util.List;

import javafx.geometry.Point2D;
import root.model.PlayerData;
import root.model.component.option.FieldOption;
import root.model.event.ModelEventHandler;

public interface Field {

	boolean isVisible();

	Unit getUnit();

	void setUnit(Unit new_unit);

	Terrain getTerrain();

	PlayerData getPlayer();

	Point2D getStoragePosition();

	void setModelEventHandler(ModelEventHandler event_handler);

	boolean isInBattle();

	// TODO maybe better solution (next 2 methods) than initializeFieldOptions
	void addFieldOptions(List<FieldOption> options);

	void addFieldOption(FieldOption option);

	List<FieldOption> getEnabledOptions();

	void adjustOptionsFor(Field second_field);

}
