package model.component.unit;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.event.MoveModelEventArg;
import root.model.action.move.MoveType;
import root.model.action.move.PathFinder;
import root.model.component.Field;

public class BasicMove extends MoveType {

	public BasicMove(Field myField,
	PathFinder pathFinder,
	ScheduledExecutorService timer) {

		super(myField, pathFinder, timer);

		this.moveDelay = this.calculateDelay();
	}

	// this implementation only starts timer for move.
	// timer will raise MoveModelEvent
	@Override
	public void move() {

		if (this.path != null && !this.path.isEmpty()) {

			// debug
			System.out.println("move from : "
					+ this.myField.getStoragePosition() +
					"->: "
					+ this.path.get(0).getStoragePosition());

			Field nextField = path.get(0);

			if (nextField.getUnit() != null) {
				// debug
				System.out.println("Recalculating path ...");
				this.path = this.pathFinder.findPath(
						this.myField,
						this.destinationField);
			}

			super.moving = true;

			calculateDelay();
			this.timer.schedule(this, moveDelay, TimeUnit.MILLISECONDS);
			// this will just raise event (at every moveDelay seconds)
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

	// TODO implement calculate delay based on current terrain
	@Override
	public int calculateDelay() {
		return 2500;
	}

	@Override
	public void run() {
		onEvent.handleModelEvent(new MoveModelEventArg(
				myField.getPlayer().getUsername(),
				myField.getStoragePosition(),
				path.get(0).getStoragePosition()));
	}

	@Override
	public void stopMoving() {

	}

}
