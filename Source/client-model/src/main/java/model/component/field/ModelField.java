package model.component.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<String, FieldOption> options;

	private ModelEventHandler event_handler;

	// methods

	public ModelField(Point2D storage_position, PlayerData player, boolean visibility, Unit unit,
			Terrain terrain) {
		super();

		this.storage_position = storage_position;
		this.player = player;
		this.visibility = visibility;
		this.unit = unit;
		this.terrain = terrain;

		this.initOptions();

	}

	private void initOptions() {

		this.options = new HashMap<String, FieldOption>();

		// TODO add some default options

	}

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

		// if method is used for setting (not removing) unit
		if (this.unit != null) {
			this.unit.setEventHandler(this.event_handler);
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
	public Map<String, FieldOption> getOptions() {
		return this.options;
	}

}
