package model.component.unit;

import java.util.TimerTask;

import model.component.Field;
import model.component.Terrain;

public class BasicMove extends MoveType {

	// don't ask how it works ... it works...
	private class DefaultTimerTask extends TimerTask {

		@Override
		public void run() {

			if (!path.isEmpty()) {

				Field next_field = path.remove(0);

				if (on_move != null)
					on_move.execute(my_field, next_field);

				my_field = next_field;
				calculate_delay();

				if (moving && !path.isEmpty()) {

					temp_timer_task = new DefaultTimerTask();
					timer.schedule(temp_timer_task, move_delay);

				}

			} else {
				// stop moving
				temp_timer_task = null;
			}
		}

	}

	private DefaultTimerTask temp_timer_task;

	public BasicMove(Field my_field) {
		super(my_field);

		this.move_delay = this.calculate_delay();

	}

	@Override
	public long calculate_delay() {

		super.move_delay = 500;
		return super.move_delay;
	}

	@Override
	public void move() {

		super.moving = true;

		this.calculate_delay();

		super.timer.schedule(new DefaultTimerTask(), super.move_delay);

	}

}
