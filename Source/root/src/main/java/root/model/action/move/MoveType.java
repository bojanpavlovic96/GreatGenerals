package root.model.action.move;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import root.model.component.Field;
import root.model.event.ModelEventHandler;
import root.model.event.MoveModelEventArg;

public abstract class MoveType implements Cloneable, Runnable {

	protected ScheduledExecutorService executor;

	protected Field my_field;
	protected Field destination_field;

	protected PathFinder path_finder;
	protected List<Field> path;
	protected boolean moving;

	protected int move_delay;

	protected ModelEventHandler on_event;

	// constructors

	public MoveType(Field my_field, PathFinder path_finder, ScheduledExecutorService executor,
			ModelEventHandler on_event) {
		super();

		this.executor = executor;
		this.my_field = my_field;
		this.path_finder = path_finder;
		this.on_event = on_event;
	}

	public MoveType(Field my_field, PathFinder path_finder, ScheduledExecutorService executor) {
		super();
		this.executor = executor;
		this.my_field = my_field;
		this.path_finder = path_finder;

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

		clone.path = new ArrayList<Field>();

		return clone;

	}

	public void run() {
		this.on_event.execute(new MoveModelEventArg(this.my_field.getPlayer().getUsername(),
													this.my_field.getStoragePosition(),
													this.path.get(0).getStoragePosition()));
	}

	public List<Field> getPath() {
		return this.path;
	}

	// abstract methods

	public abstract void move();

	public abstract int calculateDelay();

}
