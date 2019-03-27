package model.component.unit;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.event.MoveModelEventArg;
import root.model.action.move.MoveType;
import root.model.action.move.PathFinder;
import root.model.component.Field;

public class BasicMove extends MoveType {

	public BasicMove(Field my_field, PathFinder path_finder, ScheduledExecutorService executor) {
		super(my_field, path_finder, executor);

		this.move_delay = this.calculate_delay();

	}

	protected int calculate_delay() {

		super.move_delay = 1000;
		return super.move_delay;

	}

	@Override
	public void move() {

		// debug
		System.out.println("move from : " + this.my_field.getStoragePosition());
		System.out.println("move to: " + this.path.get(0).getStoragePosition());

		if (this.path != null && !this.path.isEmpty()) {
			// path exists and contains some fields

			Field next_field = path.get(0);

			if (next_field.getUnit() != null) {
				// debug
				System.out.println("Recalculating path ...");
				this.path = this.path_finder.findPath(this.my_field, this.destination_field);
			}

			super.moving = true;

			this.calculate_delay();

			this.timer.schedule(this, this.move_delay, TimeUnit.MILLISECONDS);

		}

	}

	@Override
	public MoveType clone() throws CloneNotSupportedException {
		return super.clone();
	}

	// implement calculate delay based on current terrain
	@Override
	public int calculateDelay() {
		return 1000;
	}

	@Override
	public void run() {
		this.on_event.execute(new MoveModelEventArg(this.my_field.getPlayer().getUsername(),
													this.my_field.getStoragePosition(),
													this.path.get(0).getStoragePosition()));
	}

}
