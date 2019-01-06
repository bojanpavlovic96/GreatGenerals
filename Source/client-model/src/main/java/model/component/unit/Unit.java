package model.component.unit;

public interface Unit extends Cloneable {

	String getUnitId();

	String getUnitName();
	
	boolean canMove();

	MoveType getMoveType();

	boolean haveAirAttack();

	boolean haveGroundAttack();

	Unit clone() throws CloneNotSupportedException;
}
