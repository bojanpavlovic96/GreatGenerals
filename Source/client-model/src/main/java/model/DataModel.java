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

	public List<Field> getNeighbours(Field for_field) {

		List<Field> neighbours = new ArrayList<Field>();
		Field neighbour = null;
		Point2D position = for_field.getStoragePosition();

		// up right
		neighbour = this.fields.get(new Point2D(position.getX() - 1, position.getY() + 1));
		if (neighbour != null)
			neighbours.add(neighbour);
		// right
		neighbour = this.fields.get(new Point2D(position.getX(), position.getY() + 1));
		if (neighbour != null)
			neighbours.add(neighbour);
		// down right
		neighbour = this.fields.get(new Point2D(position.getX() + 1, position.getY()));
		if (neighbour != null)
			neighbours.add(neighbour);
		// down left
		neighbour = this.fields.get(new Point2D(position.getX() + 1, position.getY() - 1));
		if (neighbour != null)
			neighbours.add(neighbour);
		// left
		neighbour = this.fields.get(new Point2D(position.getX(), position.getY() - 1));
		if (neighbour != null)
			neighbours.add(neighbour);
		// // up left
		neighbour = this.fields.get(new Point2D(position.getX() - 1, position.getY()));
		if (neighbour != null)
			neighbours.add(neighbour);

		return neighbours;
	}

}
