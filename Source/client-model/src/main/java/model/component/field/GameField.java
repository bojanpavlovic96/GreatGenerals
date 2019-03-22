package model.component.field;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import model.component.field.option.FieldOption;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.Terrain;
import root.model.component.Unit;
import root.model.event.ModelEventHandler;

public class GameField implements Field {

	private Point2D storage_position;

	private PlayerData player;

	private boolean visibility;

	private Unit unit;
	private Terrain terrain;

	// implement battle and options
	private List<Unit> units_in_battle;
	private List<FieldOption> options;

	private ModelEventHandler event_handler;

	// methods

	public GameField(Point2D storage_position, PlayerData player, boolean visibility, Unit unit,
			Terrain terrain) {
		super();

		this.storage_position = storage_position;
		this.player = player;
		this.visibility = visibility;
		this.unit = unit;
		this.terrain = terrain;

		this.units_in_battle = new ArrayList<Unit>();
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
			this.unit.setModelEventHandler(this.event_handler);
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

}
