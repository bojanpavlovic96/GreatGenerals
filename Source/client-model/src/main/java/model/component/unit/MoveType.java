package model.component.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import model.component.Field;
import model.component.Terrain;

public abstract class MoveType {

	// fields

	protected Timer timer;

	protected Field my_field;

	protected long move_delay;
	protected boolean moving;

	protected List<Field> path;
	protected MoveEventHandler on_move;

	// methods

	public MoveType(Field my_field) {
		this.my_field = my_field;
		// this.move_delay = speed;

		this.path = new ArrayList<Field>();
		this.timer = new Timer(true);
		// true means that the timer thread is going to be daemon

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

	public abstract long calculate_delay();

	public abstract void move();

}
