package model.component.unit;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.component.field.Field;
import model.path.PathFinder;

public class BasicMove extends MoveType {

	public BasicMove(Field my_field, PathFinder path_finder, ScheduledExecutorService executor) {
		super(my_field, path_finder, executor);

		this.move_delay = this.calculate_delay();

		// --- test

	}

	@Override
	public long calculate_delay() {

		super.move_delay = 1000;
		return super.move_delay;

	}

	@Override
	public void move() {
		
		super.moving = true;

		this.calculate_delay();

		this.executor.schedule(this, this.move_delay, TimeUnit.MILLISECONDS);

	}

	@Override
	public MoveType clone() throws CloneNotSupportedException {
		MoveType clone = super.clone();
		clone.setPath(new ArrayList<Field>());

		return clone;
	}

}
