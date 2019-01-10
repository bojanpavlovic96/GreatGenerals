package model.component.unit;

import model.component.field.Field;

public interface Unit extends Cloneable {

	String getUnitId();

	String getUnitName();

	boolean canMove();

	MoveType getMoveType();

	boolean haveAirAttack();

	boolean haveGroundAttack();

	Unit clone() throws CloneNotSupportedException;

	void moveTo(Field next_field);
}
