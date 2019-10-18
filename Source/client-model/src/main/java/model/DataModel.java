package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javafx.geometry.Point2D;
import model.component.unit.BasicMove;
import model.component.unit.BasicUnit;
import model.component.unit.UnitCreator;
import model.path.AStar;
import root.model.Model;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.event.ModelEventHandler;

public class DataModel implements Model {

	private Map<Point2D, Field> fields;
	private Map<String, PlayerData> players;

	private UnitCreator unit_creator;

	private ScheduledExecutorService executor;

	// unique event handler (move, attack, build ... )
	private ModelEventHandler event_handler;

	// constructors

	public DataModel() {

		// this is timer...
		this.executor = Executors.newScheduledThreadPool(3);
		// remove task from waiting queue after it is canceled
		((ScheduledThreadPoolExecutor) this.executor).setRemoveOnCancelPolicy(true);

		this.initUnitCreator();

	}

	// private methods

	private void initUnitCreator() {

		this.unit_creator = new UnitCreator();

		Unit basic_unit = new BasicUnit(new BasicMove(null, new AStar(this), this.executor), null,
				null);
		this.unit_creator.addPrototype(basic_unit);

		// TODO add some more units

	}

	// methods

	public void initializeModel(List<PlayerData> list_of_players, List<Field> fields) {

		this.players = new HashMap<String, PlayerData>();
		for (PlayerData player : list_of_players) {
			this.players.put(player.getUsername(), player);
		}

		this.fields = new HashMap<Point2D, Field>();
		for (Field field : fields) {

			// event handlers passed from controller

			field.setModelEventHandler(this.event_handler);

			this.fields.put(field.getStoragePosition(), field);

		}
	}

	@Override
	public Field getField(Point2D storage_position) {
		return this.fields.get(storage_position);
	}

	@Override
	public void setField(Field new_field) {
		this.fields.put(new_field.getStoragePosition(), new_field);
	}

	public boolean isInitialized() {
		return this.fields != null;
	}

	@Override
	public List<Field> getFields() {
		return new ArrayList<Field>(this.fields.values());
	}

	@Override
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

	public void setUnit(Point2D storage_position, String unit_name) {

		Field field = this.fields.get(storage_position);
		Unit unit = this.unit_creator.generateUnit(unit_name);

		field.setUnit(unit);

		unit.setField(field);

	}

	@Override
	public void setEventHandler(ModelEventHandler handler) {
		this.event_handler = handler;
	}

	@Override
	public void shutdown() {

		// attention this could possibly throw some exceptions if some tasks are still
		// running or waiting
		if (this.executor != null && !this.executor.isShutdown()) {
			// this shutdown cancels all running and waiting tasks
			this.executor.shutdownNow();
		}

	}

	@Override
	public Unit generateUnit(String unit_name) {
		return this.unit_creator.generateUnit(unit_name);
	}

}
