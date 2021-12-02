package root.model;

import java.util.List;

import javafx.geometry.Point2D;
import root.ActiveComponent;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.event.ModelEventHandler;

public interface Model extends ActiveComponent {

	List<Field> getFields();

	List<Field> getFreeNeighbours(Field currentField);

	Field getField(Point2D storage_position);

	void setField(Field field);

	void setEventHandler(ModelEventHandler event_handler);

	void initializeModel(List<PlayerData> list_of_players, List<Field> fields);

	Unit generateUnit(String unit_name);

}
