package model.component.unit;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import model.event.MoveModelEventArg;
import root.model.action.move.Move;
import root.model.action.move.PathFinder;
import root.model.component.Field;
import root.model.component.Terrain;
import root.model.event.Timer;

public class BasicMove extends Move implements Runnable {

	public static final String name = "BasicMove";

	protected Timer timer;

	private ScheduledFuture<?> movingFuture;

	public BasicMove(long moveDelay,
			float terrainMultiplier,
			PathFinder pathFinder,
			Timer timer) {

		super(moveDelay, terrainMultiplier, pathFinder);

		this.timer = timer;
	}

	// this implementation only starts timer for move.
	// timer will raise MoveModelEvent
	@Override
	public void move() {

		if (this.path != null && !this.path.isEmpty()) {

			// TODO this.myfield should be path[0]
			// and the next field to move at should be path[1]
			System.out.println("Intention to move: "
					// + this.myField.getStoragePosition()

					+ "->: "
					+ this.path.get(0).getStoragePosition());

			Field nextField = path.get(0);

			// // TODO this logic should be at the server side 
			// if (nextField.getUnit() != null) {
			// 	// debug
			// 	System.out.println("Recalculating path ...");
			// 	this.path = this.pathFinder.findPath(
			// 			this.myField,
			// 			this.destinationField);
			// }
			var delay = calculateDelay(nextField.getTerrain());
			movingFuture = timer.schedule((Runnable) this, delay, TimeUnit.MILLISECONDS);
			// this will just raise event (at every moveDelay seconds)
			// that unit is ready to move
			// this event is passed to the server and after confirmation from it's side
			// controller is going to actually move unit using CtrlMoveCommand

		} else {
			// debug
			System.out.println("Move called on null or empty path ... @ BasicMove.move()");
			movingFuture = null;
		}

	}

	// @Override
	// public Move clone() throws CloneNotSupportedException {
	// 	return super.clone();
	// }

	@Override
	public long calculateDelay(Terrain terrain) {
		return (long) (moveDelay * (terrainMultiplier * terrain.getIntensity()));
	}

	@Override
	public void run() {
		// onEvent.handleModelEvent(new MoveModelEventArg(
		// 		myField.getPlayer().getUsername(),
		// 		myField.getStoragePosition(),
		// 		path.get(0).getStoragePosition()));
		onEvent.handleModelEvent(new MoveModelEventArg(
				path.get(0).getPlayer().getUsername(),
				path.get(0).getStoragePosition(),
				path.get(1).getStoragePosition()));
		// TODO path indices not adjuseted just a pseudocode
	}

	@Override
	public void stopMoving() {

		if (movingFuture != null) {
			// do not interrupt if started
			movingFuture.cancel(false);
			movingFuture = null;
		}
	}

	@Override
	public boolean isMoving() {
		return (movingFuture != null);
	}

	@Override
	public String getName() {
		return BasicMove.name;
	}

}
