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

		// TODO move_delay is hardcoded
		super.move_delay = 1000;
		return super.move_delay;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see root.model.action.move.MoveType#move()
	 * 
	 * this implementation only starts timer for move. timer just raise
	 * MoveModelEvent
	 */
	@Override
	public void move() {

		if (this.path != null && !this.path.isEmpty()) {

			// debug
			System.out.println("move from : " + this.my_field.getStoragePosition() +
					"->: " + this.path.get(0).getStoragePosition());

			Field next_field = path.get(0);

			if (next_field.getUnit() != null) {
				// debug
				System.out.println("Recalculating path ...");
				this.path = this.path_finder.findPath(this.my_field, this.destination_field);
			}

			super.moving = true;

			this.calculate_delay();
			this.timer.schedule(this, this.move_delay, TimeUnit.MILLISECONDS);
			// this will just raise event (at every move_delay second)
			// that unit is ready to move
			// this event is passed to the server and after confirmation from it's side
			// controller is going to actually move unit using CtrlMoveCommand

		} else {
			// debug
			System.out.println("Move called on null or empty path ... @ BasicMove.move()");
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

	@Override
	public void stopMoving() {
		System.out.println("ERROR stop moving not implemented ... ");

	}

}
