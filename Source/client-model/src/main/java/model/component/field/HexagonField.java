package model.component.field;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import root.Point2D;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.FieldFactory;
import root.model.component.Terrain;
import root.model.component.Unit;
import root.model.component.option.FieldOption;
import root.model.event.ModelEventHandler;
import root.model.event.ModelEventProducer;

public class HexagonField implements Field {

	private Point2D storagePosition;

	private PlayerData player;

	private boolean visibility;

	private Unit unit;
	private Terrain terrain;

	// implement battle
	private List<Unit> unitsInBattle;

	// key format: name-field-option => move-to-field-option
	// private Map<String, FieldOption> options;

	private List<FieldOption> options;

	private ModelEventHandler eventHandler;

	// constructors

	public HexagonField(Point2D storagePosition,
			PlayerData player,
			boolean visibility,
			Terrain terrain) {
		super();

		this.storagePosition = storagePosition;
		this.player = player;
		this.visibility = visibility;
		this.unit = null;
		this.terrain = terrain;

	}

	public HexagonField(Point2D storagePosition,
			PlayerData player,
			boolean visibility,
			Unit unit,
			Terrain terrain) {
		super();

		this.storagePosition = storagePosition;
		this.player = player;
		this.visibility = visibility;
		this.unit = unit;
		if (this.unit != null) {
			this.unit.setField(this);
		}
		this.terrain = terrain;

	}

	// methods

	@Override
	public Point2D getStoragePosition() {

		return storagePosition;
	}

	@Override
	public Unit getUnit() {
		return this.unit;
	}

	@Override
	public void setUnit(Unit unit) {

		this.unit = unit;

		if (this.unit != null) {
			this.unit.setField(this);

			if (this.unit instanceof ModelEventProducer) {
				((ModelEventProducer) this.unit).setModelEventHandler(this.eventHandler);
			}
		}

	}

	@Override
	public Terrain getTerrain() {
		return terrain;
	}

	// field options specific
	@Override
	public boolean isVisible() {
		return this.visibility;
	}

	@Override
	public PlayerData getPlayer() {
		return this.player;
	}

	@Override
	public void setModelEventHandler(ModelEventHandler handler) {
		this.eventHandler = handler;

		if (this.unit != null && this.unit instanceof ModelEventProducer) {
			((ModelEventProducer) this.unit).setModelEventHandler(this.eventHandler);
		}

	}

	@Override
	public ModelEventHandler getModelEventHandler() {
		return this.eventHandler;
	}

	@Override
	public boolean isInBattle() {
		return false;
	}

	@Override
	public List<FieldOption> getEnabledOptions() {
		// filter enabled options
		return options.stream()
				.filter(option -> option.isEnabled())
				.collect(Collectors.toList());
	}

	@Override
	public void adjustOptionsFor(Field secondField) {

		for (FieldOption option : this.options) {
			option.setSecondaryField(secondField);

			if (option.isAdequateFor(this)) {
				option.enableOption();
			} else {
				option.disableOption();
			}

		}
	}

	@Override
	public void addFieldOptions(List<FieldOption> newOptions) {

		for (FieldOption fieldOption : newOptions) {
			this.addFieldOption(fieldOption);
		}

	}

	@Override
	public void addFieldOption(FieldOption newOption) {

		if (this.options == null) {
			this.options = new ArrayList<FieldOption>();
		}

		this.options.add(newOption);

	}

	@Override
	public List<Point2D> getNeighbours() {
		var retList = new ArrayList<Point2D>();

		var mx = storagePosition.x;
		var my = storagePosition.y;

		// up right
		retList.add(new Point2D(mx - 1, my + 1));
		// right 
		retList.add(new Point2D(mx, my + 1));
		// down right
		retList.add(new Point2D(mx + 1, my));
		// down left
		retList.add(new Point2D(mx + 1, my - 1));
		// left
		retList.add(new Point2D(mx, my - 1));
		// up left
		retList.add(new Point2D(mx - 1, my));

		return retList;
	}

	public static FieldFactory getFactory() {
		return (visibility, position, unit, terrain, owner) -> {
			return new HexagonField(position, owner, visibility, unit, terrain);
		};
	}

}
