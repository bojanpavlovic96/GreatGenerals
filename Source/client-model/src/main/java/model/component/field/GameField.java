package model.component.field;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.PlayerData;
import model.component.Terrain;
import model.component.field.option.FieldOption;
import model.component.unit.Unit;
import model.event.ModelEventHandler;

public class GameField implements Field {

	private Point2D storage_position;

	private PlayerData player;

	private boolean visibility;

	private Unit unit;
	private Terrain terrain;

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

	public Point2D getStoragePosition() {

		return storage_position;
	}

	public void setStoragePosition(Point2D storage_position) {
		this.storage_position = storage_position;
	}

	public Unit getUnit() {
		return this.unit;
	}

	public void setUnit(Unit unit) {
	
		this.unit = unit;
		
		this.unit.setModelEventHandler(this.event_handler);

	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	// battle specific

	public boolean isInBattle() {
		return this.units_in_battle.size() > 1;
	}

	public boolean haveMorePlace() {
		return this.units_in_battle.size() < 6;
	}

	public List<Unit> getBattle() {
		return this.units_in_battle;
	}

	public void addToBattle(Unit new_unit) {
		this.units_in_battle.add(new_unit);
	}

	public Unit removeFromBattle(Unit unit) {
		int index = 0;
		for (Unit b_unit : this.units_in_battle) {
			if (b_unit.equals(unit)) {

				this.units_in_battle.remove(index);
				return b_unit;
			}

			index++;
		}

		return null;
	}

	// field options specific

	public List<FieldOption> getOptions() {
		return this.options;
	}

	public void setOptions(List<FieldOption> options) {
		this.options = options;
	}

	public void addOption(FieldOption option) {
		this.options.add(option);
	}

	public void removeOption(String option_name) {
		int index = 0;
		for (FieldOption option : this.options) {

			if (option.getName().equals(option_name))

				this.options.remove(index);
			index++;

		}
	}

	public boolean isVisible() {
		return this.visibility;
	}

	public Color getPlayerColor() {
		return this.player.getPlayerColor();
	}

	public void setModelEventHandler(ModelEventHandler handler) {
		this.event_handler = handler;
	}

}
