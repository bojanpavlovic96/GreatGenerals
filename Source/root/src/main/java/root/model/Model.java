package root.model;

import java.util.List;

import root.ActiveComponent;
import root.Point2D;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.event.ModelEventHandler;

public interface Model extends ActiveComponent {

	List<Field> getFields();

	List<Field> getFreeNeighbours(Field currentField);

	Field getField(Point2D storagePosition);

	void setField(Field field);

	void setEventHandler(ModelEventHandler eventHandler);

	void initializeModel(List<PlayerData> listOfPlayers, List<Field> fields);

	Unit generateUnit(String unitName);

	

}
