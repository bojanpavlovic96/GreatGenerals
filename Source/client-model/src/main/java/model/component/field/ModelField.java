package model.component.field;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import root.Point2D;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.Terrain;
import root.model.component.Unit;
import root.model.component.option.FieldOption;
import root.model.event.ModelEventHandler;

public class ModelField implements Field {

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

	public ModelField(Point2D storagePosition,
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

	public ModelField(Point2D storage_position,
			PlayerData player,
			boolean visibility,
			Unit unit,
			Terrain terrain) {
		super();

		this.storagePosition = storage_position;
		this.player = player;
		this.visibility = visibility;
		this.unit = unit;
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

		// if method is used for inserting (not removing) unit
		if (this.unit != null) {
			this.unit.setEventHandler(this.eventHandler);
			this.unit.setField(this);
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
	}

	@Override
	public boolean isInBattle() {
		return false;
	}

	@Override
	public List<FieldOption> getEnabledOptions() {
		// filter enabled options
		return this.options.stream().filter(option -> option.isEnabled()).collect(Collectors.toList());
	}

	// implement
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

	// TODO implement
	// e.g. disable building options if terrain on this field is mountain or
	// something similar
	private void selfOptionAdjust() {

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

		// newOption.setPrimaryField(this);
		this.options.add(newOption);

		this.selfOptionAdjust();

	}

}
