package root.model.action.move;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import root.model.component.Field;
import root.model.event.ModelEventHandler;

public abstract class MoveType implements Cloneable, Runnable {

	// TODO make this base class just a holder for parameters (speed, range ... etc)
	// basic unit will be basic implementation that is using timer ... 

	// this is "timer"
	protected ScheduledExecutorService timer;

	protected Field myField;
	protected Field destinationField;

	protected PathFinder pathFinder;
	protected List<Field> path;
	// protected boolean moving;

	protected int moveDelay;

	protected ModelEventHandler onEvent;

	public MoveType(Field myField,
			PathFinder pathFinder,
			ScheduledExecutorService executor) {
		super();

		this.timer = executor;
		this.myField = myField;
		this.pathFinder = pathFinder;

		this.path = null;
	}

	public void addToPath(Field nextField) {
		if (this.path != null) {
			this.path.add(nextField);
		}
	}

	public void addToPath(List<Field> newPath) {
		if (this.path != null) {
			this.path.addAll(newPath);
		}
	}

	public int getDelay() {

		this.moveDelay = this.calculateDelay();

		return this.moveDelay;
	}

	public void setField(Field newField) {
		this.myField = newField;
	}

	public void setEventHandler(ModelEventHandler eventHandler) {
		this.onEvent = eventHandler;
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

	public abstract boolean isMoving();

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

	// TODO add isAtTheEnd method 

	public abstract void stopMoving();

	// implement calculate delay based on current terrain
	public abstract int calculateDelay();

	public abstract void run();

	public abstract String getName();

}
