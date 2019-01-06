package model;

import java.util.List;

import javafx.geometry.Point2D;
import model.component.Field;

public interface Model {

	void initializeModel(List<Field> fields);

	boolean isInitialized();

	List<Field> getFields();

	Field getField(Point2D storage_position);

	void setField(Field new_field);

	void startBattle(Field battle_field);

	List<Field> getNeighbours(Field for_field);

}
