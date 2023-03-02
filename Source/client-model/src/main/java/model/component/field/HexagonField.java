package model.component.field;

import java.util.ArrayList;
import java.util.Arrays;
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

	// storage_position.x -> q - column
	// storage_position.y -> r - row

	// Neighbours have to be in this order.
	// counter-clockwise starting from the bottom left corner	
	private final Point2D[] neighboursOffsets = {
			new Point2D(1, 0), // right 
			new Point2D(1, -1), // top right
			new Point2D(0, -1), // top left
			new Point2D(-1, 0), // left
			new Point2D(-1, +1), // down left
			new Point2D(0, 1) // down right
	};

	private Point2D storagePosition;

	private PlayerData player;

	private boolean visibility;

	private Unit unit;
	private Terrain terrain;

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
	public List<FieldOption> getEnabledOptions() {
		// filter enabled options
		return options.stream()
				.filter(option -> option.isEnabled())
				.collect(Collectors.toList());
	}

	@Override
	public void adjustOptionsFor(Field secondField) {

		// options.stream()
		// 	.filter((option) -> {
		// 		option.setSecondaryField(secondField);
		// 		return option.isAdequateFor(this);
		// 	})
		// 	.

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

	private Point2D getNeighbour(Point2D from, int i) {
		return new Point2D(
				from.x + neighboursOffsets[i].x,
				from.y + neighboursOffsets[i].y);
	}

	@Override
	public List<Point2D> getNeighbours(int r) {
		if (r == 0) {
			return Arrays.asList(storagePosition);
		}

		var retList = new ArrayList<Point2D>();

		var f = storagePosition;
		for (var i = 0; i < r; i++) {
			f = getNeighbour(f, 4);
			// Start from the bottom left corner.
			// 4 is for that direction.
		}

		var point = f;
		for (var i = 0; i < 6; i++) {
			var dir = neighboursOffsets[i];

			for (var j = 0; j < r; j++) {
				retList.add(point);
				point = new Point2D(point.x + dir.x, point.y + dir.y);
			}
		}

		return retList;
	}

	public static FieldFactory getFactory() {
		return (visibility, position, unit, terrain, owner) -> {
			return new HexagonField(position, owner, visibility, unit, terrain);
		};
	}

}
