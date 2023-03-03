package model.component.unit;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import model.intention.MoveIntention;
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

		if (path == null || path.isEmpty()) {
			// debug
			System.out.println("Move called on null or empty path ... @ BasicMove.move()");
			movingFuture = null;

			return;
		}

		System.out.println("Intention to move: "
				+ this.path.get(0).getStoragePosition()
				+ "->: "
				+ this.path.get(1).getStoragePosition());

		Field nextField = path.get(1);

		var delay = calculateDelay(nextField.getTerrain());
		System.out.println("Calculated delay: " + delay);
		movingFuture = timer.schedule((Runnable) this, delay, TimeUnit.MILLISECONDS);
		// this will just raise event (at every moveDelay seconds)
		// that unit is ready to move
		// this event is passed to the server and after confirmation from it's side
		// controller is going to actually move unit using CtrlMoveCommand

	}

	@Override
	public long calculateDelay(Terrain terrain) {
		return (long) (moveDelay * (terrainMultiplier * terrain.getIntensity()));
	}

	@Override
	public void run() {
		System.out.println("Move event raised ... ");
		movingFuture = null;

		onEvent.handleModelEvent(new MoveIntention(
				path.get(0).getPlayer().getUsername(),
				path.get(0).getStoragePosition(),
				path.get(1).getStoragePosition()));
	}

	@Override
	public void stopMoving() {
		if (movingFuture != null) {
			movingFuture.cancel(false); // do not interrupt if started
		}
		movingFuture = null;
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
