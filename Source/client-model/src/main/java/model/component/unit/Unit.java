package model.component.unit;

import model.component.field.Field;

public interface Unit extends Cloneable {

	String getUnitId();

	String getUnitName();

	boolean canMove();

	MoveType getMoveType();

	void moveTo(Field next_field);

	boolean haveAirAttack();

	boolean haveGroundAttack();

	Unit clone() throws CloneNotSupportedException;

}
