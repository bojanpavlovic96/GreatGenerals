package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javafx.geometry.Point2D;
import model.component.Field;
import model.component.unit.BasicMove;
import model.component.unit.BasicUnit;
import model.component.unit.MoveEventHandler;
import model.component.unit.Unit;
import model.component.unit.UnitCreator;

public class DataModel implements Model {

	private Map<Point2D, Field> fields;

	private UnitCreator unit_creator;

	// let's say it works
	private MoveEventHandler default_move_event_handler;

	private Timer timer;

	// methods

	public DataModel() {
		this.fields = new HashMap<Point2D, Field>();

		this.timer = new Timer(true);
		// true means that timer threads are running as daemons
		// timer is shared with all units

		this.initUnitCreator();

	}

	public DataModel(List<Field> fields) {
		this();

		for (Field field : fields) {

			this.fields.put(field.getStoragePosition(), field);

		}

	}

	private void initUnitCreator() {
		this.unit_creator = new UnitCreator();
		Unit basic_unit = new BasicUnit("first-unit", new BasicMove(null, this.timer), null, null);

		this.unit_creator.addPrototype(basic_unit);
		// only basic unit for now

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

	public List<Field> getFreeNeighbours(Field for_field) {

		List<Field> neighbours = new ArrayList<Field>();
		Field neighbour = null;
		Point2D position = for_field.getStoragePosition();

		// up right
		neighbour = this.fields.get(new Point2D(position.getX() - 1, position.getY() + 1));
		if (neighbour != null && !neighbour.isInBattle() && neighbour.getUnit() == null)
			neighbours.add(neighbour);
		// right
		neighbour = this.fields.get(new Point2D(position.getX(), position.getY() + 1));
		if (neighbour != null && !neighbour.isInBattle() && neighbour.getUnit() == null)
			neighbours.add(neighbour);
		// down right
		neighbour = this.fields.get(new Point2D(position.getX() + 1, position.getY()));
		if (neighbour != null && !neighbour.isInBattle() && neighbour.getUnit() == null)
			neighbours.add(neighbour);
		// down left
		neighbour = this.fields.get(new Point2D(position.getX() + 1, position.getY() - 1));
		if (neighbour != null && !neighbour.isInBattle() && neighbour.getUnit() == null)
			neighbours.add(neighbour);
		// left
		neighbour = this.fields.get(new Point2D(position.getX(), position.getY() - 1));
		if (neighbour != null && !neighbour.isInBattle() && neighbour.getUnit() == null)
			neighbours.add(neighbour);
		// // up left
		neighbour = this.fields.get(new Point2D(position.getX() - 1, position.getY()));
		if (neighbour != null && !neighbour.isInBattle() && neighbour.getUnit() == null)
			neighbours.add(neighbour);

		return neighbours;
	}

	public void setUnit(Point2D position, String unit_name) {

		Field field = this.fields.get(position);
		Unit unit = this.unit_creator.generateUnit(unit_name, field,
				this.default_move_event_handler /* attack handler and ground attack handler */);

		field.setUnit(unit);

	}

	public MoveEventHandler getDefaultMoveEventHandler() {
		return default_move_event_handler;
	}

	public void setDefaultMoveEventHandler(MoveEventHandler default_move_event_handler) {
		this.default_move_event_handler = default_move_event_handler;
	}

}
