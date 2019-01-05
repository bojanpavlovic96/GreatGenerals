package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Point2D;
import model.component.Field;

public class DataModel implements Model {

	private Map<Point2D, Field> fields;

	public DataModel() {
		this.fields = new HashMap<Point2D, Field>();
	}

	public DataModel(List<Field> fields) {
		this();

		for (Field field : fields) {

			this.fields.put(field.getStoragePosition(), field);

		}

	}

	public void initializeModel(List<Field> fields) {

		for (Field field : fields) {

			this.fields.put(field.getStoragePosition(), field);

		}
	}

	public Field getField(Point2D storage_position) {
		return this.fields.get(storage_position);
	}

	public void setField(Field new_field) {
		this.fields.put(new_field.getStoragePosition(), new_field);
	}

	public void startBattle(Field battle_field) {
		// TODO Auto-generated method stub

	}

	public boolean isInitialized() {
		return this.fields != null;
	}

	public List<Field> getFields() {
		return new ArrayList<Field>(this.fields.values());
	}

}
