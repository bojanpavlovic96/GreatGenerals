package model.component.unit;

import java.util.List;
import java.util.TimerTask;

import model.component.field.Field;

public class DefaultMoveTimerTask extends TimerTask {

	private Field my_field;

	// just extracted from my_field unit
	private Unit unit;
	private List<Field> path;
	private MoveEventHandler on_move;

	public DefaultMoveTimerTask(Field field) {

		this.my_field = field;

		this.unit = this.my_field.getUnit();
		this.path = this.unit.getMoveType().getPath();
		this.on_move = this.unit.getMoveType().getOnMoveHandler();

	}

	@Override
	public void run() {
		if (!path.isEmpty()) {

			Field next_field = path.remove(0);

			if (next_field.getUnit() != null) {
				System.err.println("Reaclculating path..");
				this.unit.getMoveType().calculatePath(this.unit.getMoveType().getDestination());
				if (this.unit.getMoveType().getPath() == null)
					return;
				next_field = this.unit.getMoveType().getPath().remove(0);
			}

			this.unit.moveTo(next_field);

			if (this.on_move != null) {
				System.out.println("move handler call ...");
				on_move.execute(this.my_field, next_field);
			}
			this.unit.getMoveType().calculate_delay();

			if (this.unit.getMoveType().moving && !path.isEmpty()) {

				this.unit.getMoveType().setTemp_timer_task(new DefaultMoveTimerTask(next_field));
				this.unit.getMoveType().defaultSchedule();

			} else {
				this.unit.getMoveType().setTemp_timer_task(null);

			}

		} else {
			// stop moving
			this.unit.getMoveType().setTemp_timer_task(null);
		}
	}

}
