package root.model.component;

import java.util.List;

import root.Point2D;
import root.model.PlayerData;
import root.model.component.option.FieldOption;
import root.model.event.ModelEventProducer;

public interface Field extends ModelEventProducer {

	boolean isVisible();

	Unit getUnit();

	void setUnit(Unit newUnit);

	Terrain getTerrain();

	PlayerData getPlayer();

	Point2D getStoragePosition();

	boolean isInBattle();

	void addFieldOptions(List<FieldOption> options);

	void addFieldOption(FieldOption option);

	List<FieldOption> getEnabledOptions();

	void adjustOptionsFor(Field secondField);

	List<Point2D> getNeighbours();
}
