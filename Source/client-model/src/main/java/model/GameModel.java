package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.component.unit.UnitFactory;
import model.path.AStar;
import root.ActiveComponent;
import root.Point2D;
import root.communication.PlayerDescription;
import root.communication.messages.components.AttackDesc;
import root.communication.messages.components.FieldDesc;
import root.communication.messages.components.MoveDesc;
import root.communication.messages.components.TerrainDesc;
import root.communication.messages.components.UnitDesc;
import root.model.Model;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.FieldFactory;
import root.model.component.Terrain;
import root.model.component.Unit;
import root.model.component.UnitType;
import root.model.component.Terrain.TerrainType;
import root.model.component.option.FieldOption;
import root.model.event.ModelEventHandler;
import root.model.event.ModelEventProducer;
import root.model.event.Timer;

public class GameModel implements Model {

	private Map<Point2D, Field> fields;
	private Map<String, PlayerData> players;
	private String owner;

	private List<Unit> activeUnits;

	private UnitFactory unitFactory;

	private Timer timer;

	private ModelEventHandler eventHandler;

	private FieldFactory fieldFactory;

	public GameModel(String owner, Timer timer, FieldFactory fieldFactory) {
		this.owner = owner;
		this.timer = timer;
		this.fieldFactory = fieldFactory;
	}

	@Override
	public void initializeModel(List<PlayerDescription> playersDesc,
			List<UnitDesc> units,
			List<MoveDesc> moves,
			List<AttackDesc> attacks,
			List<FieldDesc> fields,
			List<FieldOption> options) {

		this.players = new HashMap<String, PlayerData>();
		for (var player : mapPlayers(playersDesc)) {
			this.players.put(player.getUsername(), player);
		}

		this.unitFactory = new UnitFactory(units,
				moves,
				attacks,
				new AStar(),
				timer);

		this.fields = new HashMap<>();
		for (var field : mapFields(fields)) {

			field.addFieldOptions(options);

			if (field instanceof ModelEventProducer) {
				((ModelEventProducer) field).setModelEventHandler(eventHandler);
			}

			// if (field.getUnit() != null && field.getUnit() instanceof ModelEventProducer) {
			// 	((ModelEventProducer) field.getUnit()).setModelEventHandler(eventHandler);
			// }

			this.fields.put(field.getStoragePosition(), field);

		}

	}

	private List<PlayerData> mapPlayers(List<PlayerDescription> players) {
		return players.stream()
				.map((pd) -> mapPlayer(pd))
				.collect(Collectors.toList());

	}

	private PlayerData mapPlayer(PlayerDescription player) {
		return new PlayerModelData(player.getUsername(),
				player.getColor(),
				player.getLevel(),
				player.getPoints());
	}

	private List<Field> mapFields(List<FieldDesc> descs) {

		var retList = new ArrayList<Field>();

		for (var desc : descs) {

			var fieldOwner = players.get(desc.owner);

			var field = fieldFactory.getField(desc.isVisible,
					desc.position,
					mapUnit(desc.unitName, fieldOwner),
					mapTerrain(desc.terrain),
					fieldOwner);

			retList.add(field);
		}

		return retList;
	}

	private Unit mapUnit(String unitType, PlayerData owner) {
		if (unitType != null) {
			return this.generateUnit(UnitType.valueOf(unitType), owner);
		} else {
			return null;
		}
	}

	private Terrain mapTerrain(TerrainDesc desc) {
		return new Terrain(
				TerrainType.valueOf(desc.type),
				desc.intensity);
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
	public List<Field> getFreeNeighbours(Field forField, int range) {

		var neighbours = new ArrayList<Field>();
		for (var nPoint : forField.getNeighbours(range)) {

			var field = getField(nPoint);

			if (field != null && field.getUnit() == null) {
				neighbours.add(field);
			}

		}

		return neighbours;
	}

	@Override
	public void setModelEventHandler(ModelEventHandler handler) {
		this.eventHandler = handler;
	}

	@Override
	public ModelEventHandler getModelEventHandler() {
		return this.eventHandler;
	}

	@Override
	public void shutdown() {

		if (timer instanceof ActiveComponent) {
			((ActiveComponent) timer).shutdown();
		}

	}

	@Override
	public root.model.component.Unit generateUnit(UnitType type, PlayerData owner) {
		var unit = unitFactory.generateUnit(type, owner);

		if (activeUnits == null) {
			activeUnits = new ArrayList<Unit>();
		}

		activeUnits.add(unit);

		return unit;
	}

	@Override
	public boolean destroyUnit(Unit unit) {
		return activeUnits.remove(unit);
	}

	@Override
	public int distance(Field aField, Field bField) {
		// this should be inside the fieldManager
		var a = aField.getStoragePosition();
		var b = bField.getStoragePosition();

		return (int) (Math.abs(a.getX() - b.getX()) +
				Math.abs(a.getX() + a.getY() - b.getX() - b.getY()) +
				Math.abs(a.getY() - b.getY()))
				/ 2;
	}

	@Override
	public List<Unit> getActiveUnits() {
		return this.activeUnits;
	}

	@Override
	public void removeUnit(Unit unit) {
		this.activeUnits.remove(unit);
	}

	@Override
	public PlayerData getPlayer(String name) {
		return players.get(name);
	}

	@Override
	public PlayerData getOwner() {
		return players.get(owner);
	}

	@Override
	public List<UnitDesc> getUnits() {
		return this.unitFactory.getUnits();
	}

}
