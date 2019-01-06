package model.component.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import model.component.Field;

public abstract class MoveType implements Cloneable {

	// fields

	protected Timer timer;
	protected DefaultMoveTimerTask temp_timer_task;

	protected Field my_field;

	protected long move_delay;
	protected boolean moving;

	protected List<Field> path;
	protected MoveEventHandler on_move;

	// methods

	public MoveType(Field my_field, Timer move_timer) {
		this.setMy_field(my_field);
		// this.move_delay = speed;

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

	public void addToPath(List<Field> fields) {
		this.path.addAll(fields);
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

	public Field getMy_field() {
		return my_field;
	}

	public void setMy_field(Field my_field) {
		this.my_field = my_field;
	}

	public abstract long calculate_delay();

	public abstract void move();

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
