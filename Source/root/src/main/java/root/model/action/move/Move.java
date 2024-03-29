package root.model.action.move;

import java.util.List;

import root.model.Model;
import root.model.component.Field;
import root.model.component.Terrain;
import root.model.event.ModelEventHandler;
import root.model.event.ModelEventProducer;

public abstract class Move implements ModelEventProducer {

	protected PathFinder pathFinder;
	// Since every check is done by the server side this could be simply be 
	// just the list of Point2D ... ? 
	protected List<Field> path;

	protected long moveDelay; // in ms
	protected float terrainMultiplier;

	protected ModelEventHandler onEvent;

	public Move(long moveDelay,
			float terrainMultiplier,
			PathFinder pathFinder) {

		this.moveDelay = moveDelay;
		this.terrainMultiplier = terrainMultiplier;

		this.pathFinder = pathFinder;
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

	public float getDelay() {
		return this.moveDelay;
	}

	@Override
	public ModelEventHandler getModelEventHandler() {
		return this.onEvent;
	}

	@Override
	public void setModelEventHandler(ModelEventHandler handler) {
		this.onEvent = handler;
	}

	public List<Field> getPath() {
		return this.path;
	}

	public List<Field> calculatePath(Model model, Field startField, Field targetField) {

		this.path = pathFinder.findPath(model, startField, targetField);

		return this.path;
	}

	public PathFinder getPathFinder() {
		return this.pathFinder;
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

	public void clearPath() {
		if (isMoving()) {
			stopMoving();
		}

		if (path != null) {
			path.clear();
			path = null;
		}
	}

	// abstract methods

	public abstract void move();

	public abstract boolean isMoving();

	public abstract void stopMoving();

	public abstract long calculateDelay(Terrain terrain);

	public abstract String getName();

}
