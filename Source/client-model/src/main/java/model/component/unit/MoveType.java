package model.component.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import model.component.field.Field;
import model.event.ModelEventHandler;
import model.event.MoveModelEventArg;
import model.path.PathFinder;

public abstract class MoveType implements Cloneable, Runnable {

	// fields

	protected ScheduledExecutorService executor;

	protected PathFinder path_finder;

	protected Field my_field;

	protected long move_delay;
	protected boolean moving;

	protected List<Field> path;

	protected ModelEventHandler on_event;

	// methods

	public MoveType(Field my_field, PathFinder path_finder, ScheduledExecutorService executor) {
		super();

		this.setField(my_field);
		this.path_finder = path_finder;

		this.path = new ArrayList<Field>();
		this.executor = executor;
		// timer passed from model

	}

	public List<Field> getPath() {
		return this.path;
	}

	public void setPath(List<Field> path) {
		this.path = path;
	}

	public void addToPath(Field field) {
		this.path.add(field);
	}

	public Field getDestination() {
		if (this.path != null && this.path.size() > 0) {
			return this.path.get(this.path.size() - 1);
		}
		return null;
	}

	public void addToPath(List<Field> fields) {
		this.path.addAll(fields);
	}

	public void calculatePath(Field destination) {
		this.path = this.path_finder.findPath(this.my_field, destination);
	}

	public long getDelay() {
		return this.move_delay;
	}

	public void setDelay(long speed) {
		this.move_delay = speed;
	}

	public void setEventHandelr(ModelEventHandler handler) {
		this.on_event = handler;
	}

	public ModelEventHandler getEventHandler() {
		return this.on_event;
	}

	public Field getMyField() {
		return my_field;
	}

	public void setField(Field my_field) {
		this.my_field = my_field;
	}

	// abstract

	public abstract long calculate_delay();

	public abstract void move();

	// abstract

	// timer task
	@Override
	public void run() {


		this.on_event.execute(new MoveModelEventArg(this.my_field.getPlayer().getUsername(),
													this.my_field.getStoragePosition(),
													this.path.get(0).getStoragePosition()));

	}

	public MoveType clone() throws CloneNotSupportedException {
		return (MoveType) super.clone();
	}

}
