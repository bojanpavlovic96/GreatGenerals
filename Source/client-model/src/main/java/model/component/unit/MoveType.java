package model.component.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import model.component.field.Field;
import model.path.PathFinder;

public abstract class MoveType implements Cloneable {

	// fields

	protected Timer timer;
	protected DefaultMoveTimerTask temp_timer_task;

	protected PathFinder path_finder;

	protected Field my_field;

	protected long move_delay;
	protected boolean moving;

	protected List<Field> path;
	protected MoveEventHandler on_move;

	// methods

	public MoveType(Field my_field, PathFinder path_finder, Timer move_timer) {
		this.setMyField(my_field);
		// this.move_delay = speed;

		this.path_finder = path_finder;

		this.path = new ArrayList<Field>();
		this.timer = move_timer;
		// data_model timer

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

	public void setOnMoveHandler(MoveEventHandler move_handler) {
		this.on_move = move_handler;
	}

	public MoveEventHandler getOnMoveHandler() {
		return this.on_move;
	}

	public Field getMyField() {
		return my_field;
	}

	public void setMyField(Field my_field) {
		this.my_field = my_field;
	}

	// abstract
	public abstract long calculate_delay();

	public abstract void move();

	// abstract

	public DefaultMoveTimerTask getTemp_timer_task() {
		return temp_timer_task;
	}

	public void setTemp_timer_task(DefaultMoveTimerTask temp_timer_task) {
		this.temp_timer_task = temp_timer_task;
	}

	public void defaultSchedule() {
		this.timer.schedule(this.temp_timer_task, this.move_delay);
	}

	public MoveType clone() throws CloneNotSupportedException {
		return (MoveType) super.clone();
	}

}
