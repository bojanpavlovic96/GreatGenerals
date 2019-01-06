package model.component.unit;

public interface Unit {

	String getUnitId();

	String getUnitName();

	boolean canMove();

	MoveType getMoveType();

	boolean haveAirAttack();

	boolean haveGroundAttack();

}
