package root.model.component;

import java.util.List;

import root.model.PlayerData;
import root.model.action.attack.Attack;
import root.model.action.move.Move;

public interface Unit {

	PlayerData getOwner();

	UnitType getUnitType();

	Field getField();

	void setField(Field field);

	boolean canMove();

	Move getMove();

	// void relocateTo(Field nextField);

	List<Attack> getAttacks();

	boolean hasAttacks();

	boolean hasAttack(String type);

	void activateAttack(Attack attack);

	Attack getActiveAttack();

	boolean isAttacking(); // whom and using which attack to be added

}
