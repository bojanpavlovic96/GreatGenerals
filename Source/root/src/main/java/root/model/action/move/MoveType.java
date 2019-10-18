package root.model.action.move;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import root.model.component.Field;
import root.model.event.ModelEventHandler;

public abstract class MoveType implements Cloneable, Runnable {

	// this is "timer"
	protected ScheduledExecutorService timer;

	protected Field my_field;
	protected Field destination_field;

	protected PathFinder path_finder;
	protected List<Field> path;
	protected boolean moving;

	protected int move_delay;

	protected ModelEventHandler on_event;

	// constructors

	public MoveType(Field my_field, PathFinder path_finder, ScheduledExecutorService executor) {
		super();

		this.timer = executor;
		this.my_field = my_field;
		this.path_finder = path_finder;

		this.path = null;

	}

	// methods

	// implement calculate delay based on current terrain

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

		this.move_delay = this.calculateDelay();

		return this.move_delay;
	}

	public void setField(Field new_field) {
		this.my_field = new_field;
	}

	public void setEventHandler(ModelEventHandler event_handler) {
		this.on_event = event_handler;
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

	public List<Field> calculatePath(Field target_field) {

		this.destination_field = target_field;
		this.path = this.path_finder.findPath(this.my_field, target_field);

		return this.path;
	}

	// abstract methods

	public abstract void move();

	public abstract int calculateDelay();

	public abstract void run();

}
