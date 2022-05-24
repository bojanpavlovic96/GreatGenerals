package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import model.component.unit.BasicMove;
import model.component.unit.BasicUnit;
import model.component.unit.UnitFactory;
import model.path.AStar;
import root.Point2D;
import root.model.Model;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.event.ModelEventHandler;

public class DataModel implements Model {

	private Map<Point2D, Field> fields;
	private Map<String, PlayerData> players;

	private UnitFactory unitFactory;

	// this is timer ...
	private ScheduledExecutorService executor;

	// unique event handler (move, attack, build ... )
	private ModelEventHandler eventHandler;

	public DataModel() {

		this.executor = Executors.newScheduledThreadPool(3);
		// remove task from waiting queue after it is cancelled
		((ScheduledThreadPoolExecutor) this.executor).setRemoveOnCancelPolicy(true);

		this.initUnitCreator();

	}

	// TODO MAYYYBE provide unitFactory as and external dependency ... ? 
	private void initUnitCreator() {

		this.unitFactory = new UnitFactory();

		Unit basicUnit = new BasicUnit(
				new BasicMove(null, new AStar(this), this.executor),
				new ArrayList<>());

		this.unitFactory.addPrototype(basicUnit);

	}

	public void initializeModel(List<PlayerData> listOfPlayers, List<Field> fields) {

		this.players = new HashMap<String, PlayerData>();
		for (PlayerData player : listOfPlayers) {
			this.players.put(player.getUsername(), player);
		}

		this.fields = new HashMap<Point2D, Field>();
		for (Field field : fields) {

			// event handlers passed from controller

			field.setModelEventHandler(this.eventHandler);

			this.fields.put(field.getStoragePosition(), field);

		}
	}

	@Override
	public Field getField(Point2D storagePosition) {
		return fields.get(storagePosition);
	}

	@Override
	public void setField(Field new_field) {
		fields.put(new_field.getStoragePosition(), new_field);
	}

	@Override
	public List<Field> getFields() {
		return new ArrayList<Field>(this.fields.values());
	}

	@Override
	public List<Field> getFreeNeighbours(Field forField) {

		// TODO this is tied to the hexagon fields implementation 
		var neighbours = new ArrayList<Field>();
		Field neighbour = null;
		Point2D position = forField.getStoragePosition();

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
		// up left
		neighbour = this.fields.get(new Point2D(position.getX() - 1, position.getY()));
		if (neighbour != null && !neighbour.isInBattle() && neighbour.getUnit() == null)
			neighbours.add(neighbour);

		return neighbours;
	}

	// public void setUnit(Point2D storagePosition, String unitName) {

	// 	Field field = this.fields.get(storagePosition);
	// 	Unit unit = this.unitFactory.generateUnit(unitName);

	// 	if (unit != null) {

	// 		field.setUnit(unit);
	// 		unit.setField(field);

	// 	} else {
	// 		System.out.println("Unit creator was unable to create requested unit " + unitName);
	// 	}

	// }

	@Override
	public void setEventHandler(ModelEventHandler handler) {
		this.eventHandler = handler;
	}

	@Override
	public void shutdown() {

		// attention this could possibly throw some exceptions if some tasks are still
		// running or waiting
		if (this.executor != null&& !this.executor.isShutdown()) {

			// this shutdown cancels all running and waiting tasks
			this.executor.shutdownNow();
		}

	}

	@Override
	public Unit generateUnit(String unit_name) {
		return this.unitFactory.generateUnit(unit_name);
	}

}
