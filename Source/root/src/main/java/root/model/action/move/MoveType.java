package root.model.action.move;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import root.model.component.Field;
import root.model.event.ModelEventHandler;

public abstract class MoveType implements Cloneable, Runnable {

	// this is "timer"
	protected ScheduledExecutorService timer;

	protected Field myField;
	protected Field destinationField;

	protected PathFinder pathFinder;
	protected List<Field> path;
	protected boolean moving;

	protected int moveDelay;

	protected ModelEventHandler onEvent;

	// constructors

	public MoveType(Field my_field,
			PathFinder path_finder,
			ScheduledExecutorService executor) {
		super();

		this.timer = executor;
		this.myField = my_field;
		this.pathFinder = path_finder;

		this.path = null;
	}

	// methods

	public void addToPath(Field next_field) {
		if (this.path != null) {
			this.path.add(next_field);
		}
	}

	public void addToPath(List<Field> next_path) {
		if (this.path != null) {
			this.path.addAll(next_path);
		}
	}

	public int getDelay() {

		this.moveDelay = this.calculateDelay();

		return this.moveDelay;
	}

	public void setField(Field new_field) {
		this.myField = new_field;
	}

	public void setEventHandler(ModelEventHandler event_handler) {
		this.onEvent = event_handler;
	}

	@Override
	public MoveType clone() throws CloneNotSupportedException {
		MoveType clone = (MoveType) super.clone();

		clone.path = null;

		return clone;

	}

	public List<Field> getPath() {
		return this.path;
	}

	public List<Field> calculatePath(Field targetField) {

		this.destinationField = targetField;
		this.path = this.pathFinder.findPath(this.myField, targetField);

		return this.path;
	}

	public PathFinder getPathFinder() {
		return this.pathFinder;
	}

	public boolean isMoving() {
		return moving;
	}

	public boolean isOnPath(Field field) {
		var fieldPos = field.getStoragePosition();

		if (this.path == null || this.path.size() == 0) {
			return false;
		}

		for (Field pathField : this.path) {
			var pathFieldPos = pathField.getStoragePosition();

			if (fieldPos.getX() == pathFieldPos.getX()
					&& fieldPos.getY() == pathFieldPos.getY()) {
				return true;
			}

		}

		return false;
	}

	// abstract methods

	public abstract void move();

	public abstract void stopMoving();

	// implement calculate delay based on current terrain
	public abstract int calculateDelay();

	public abstract void run();

}
