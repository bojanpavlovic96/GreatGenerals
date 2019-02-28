package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javafx.geometry.Point2D;
import model.component.field.Field;
import model.component.unit.BasicMove;
import model.component.unit.BasicUnit;
import model.component.unit.MoveEventHandler;
import model.component.unit.Unit;
import model.component.unit.UnitCreator;
import model.path.AStar;

public class DataModel implements Model {

	private Map<Point2D, Field> fields;
	private Map<String, PlayerData> players;

	private UnitCreator unit_creator;

	// attention there should be unique modelEvent for all events (move, attack ...)
	private MoveEventHandler default_move_event_handler;

	private Timer timer;

	// methods

	// constructors

	public DataModel() {

		this.timer = new Timer(true);
		// true means that timer threads are running as daemons
		// timer is shared with all units

		this.initUnitCreator();

	}

	// private methods

	private void initUnitCreator() {

		this.unit_creator = new UnitCreator();

		Unit basic_unit = new BasicUnit(new BasicMove(null, new AStar(this), this.timer), null, null);
		basic_unit.getMoveType().setOnMoveHandler(this.default_move_event_handler);

		this.unit_creator.addPrototype(basic_unit);
		// only basic unit for now

		// TODO add some more units

	}

	// public methods

	public void initializeModel(List<PlayerData> players, List<Field> fields) {

		this.fields = new HashMap<Point2D, Field>();

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
		Unit unit = this.unit_creator
				.generateUnit(	unit_name,
								field,
								this.default_move_event_handler /* attack handler and ground attack handler */);
		// prototype doesn't have handlers because they are set after creators
		// initialization

		field.setUnit(unit);

	}

	public MoveEventHandler getDefaultMoveEventHandler() {
		return default_move_event_handler;
	}

	public void setDefaultMoveEventHandler(MoveEventHandler default_move_event_handler) {
		this.default_move_event_handler = default_move_event_handler;
	}

}
