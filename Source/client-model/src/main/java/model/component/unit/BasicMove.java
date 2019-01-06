package model.component.unit;

import java.util.ArrayList;
import java.util.Timer;

import model.component.Field;

public class BasicMove extends MoveType {

	public BasicMove(Field my_field, Timer move_timer) {
		super(my_field, move_timer);

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

		super.timer.schedule(new DefaultMoveTimerTask(super.my_field), super.move_delay);

	}

	@Override
	public MoveType clone() throws CloneNotSupportedException {
		MoveType clone = super.clone();
		clone.setPath(new ArrayList<Field>());

		return clone;
	}

}
