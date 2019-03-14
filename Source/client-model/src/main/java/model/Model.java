package model;

import java.util.List;

import javafx.geometry.Point2D;
import model.component.field.Field;
import model.event.ModelEventHandler;

public interface Model {

	void initializeModel(List<PlayerData> players, List<Field> fields);

	boolean isInitialized();

	List<Field> getFields();

	Field getField(Point2D storage_position);

	void setField(Field new_field);

	List<Field> getFreeNeighbours(Field for_field);

	void setUnit(Point2D position, String unit_name);

	void setModelEventHandler(ModelEventHandler handler);

	// TODO extract this in some other interface
	// existing ShouldBeShutDown interface is from client-view ... that is not good
	// ... nop nop
	void shutdown();

}
