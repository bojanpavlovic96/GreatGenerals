package model.component.field;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Point2D;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.Terrain;
import root.model.component.Unit;
import root.model.component.option.FieldOption;
import root.model.event.ModelEventHandler;

public class ModelField implements Field {

	private Point2D storage_position;

	private PlayerData player;

	private boolean visibility;

	private Unit unit;
	private Terrain terrain;

	// implement battle
	private List<Unit> units_in_battle;

	// key format: name-field-option => move-to-field-option
	// private Map<String, FieldOption> options;

	private List<FieldOption> options;

	private ModelEventHandler event_handler;

	// constructors

	public ModelField(Point2D storage_position, PlayerData player, boolean visibility, Unit unit,
			Terrain terrain) {
		super();

		this.storage_position = storage_position;
		this.player = player;
		this.visibility = visibility;
		this.unit = unit;
		this.terrain = terrain;

	}

	// methods

	@Override
	public Point2D getStoragePosition() {

		return storage_position;
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
			// update unit event handler (modelEventHandler)
			this.unit.setEventHandler(this.event_handler);
			// update reference to field
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
		this.event_handler = handler;
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
	public List<FieldOption> adjustOptionsFor(Field second_field) {

		// attention always check is second field null

		// debug
		System.out.println("Adjusting field options ...");

		for (FieldOption option : this.options) {
			option.setSecondaryField(second_field);
		}

		// restore all disabled options if there are any

		// check what is available
		// save every disabled option

		return this.options;

	}

	// implement
	private void selfOptionAdjust() {

	}

	@Override
	public void addFieldOptions(List<FieldOption> newOptions) {

		if (this.options == null) {
			this.options = new ArrayList<FieldOption>();
		}

		for (FieldOption option : newOptions) {
			option.setPrimaryField(this); // attention this line should be moved in to the selfOptionAdjust
											 // maybe
			this.options.add(option);
		}

		this.selfOptionAdjust();

	}

	@Override
	public void addFieldOption(FieldOption newOption) {

		if (this.options == null) {
			this.options = new ArrayList<FieldOption>();
		}

		newOption.setPrimaryField(this);
		this.options.add(newOption);

		this.selfOptionAdjust();

	}

}
