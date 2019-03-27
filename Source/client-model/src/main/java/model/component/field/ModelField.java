package model.component.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Point2D;
import root.controller.Controller;
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
	private Map<String, FieldOption> options;

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
		return new ArrayList<FieldOption>(this.options.values());
	}

	// implement
	@Override
	public void adjustOptionsFor(Field second_field) {

		// debug
		System.out.println("ADJUSTING OPTOIONS ...");

		for (FieldOption option : this.options.values()) {
			option.setSecondaryField(second_field);
		}

		// restore all disabled options if there are any

		// check what is available
		// save every disabled option

	}

	@Override
	public void initializeOptions(List<FieldOption> new_options) {

		this.options = new HashMap<String, FieldOption>();

		for (FieldOption option : new_options) {
			this.options.put(option.getName(), option);
		}

		this.selfOptionAdjust();

	}

	// implement
	private void selfOptionAdjust() {

	}

}
