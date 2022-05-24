package root.model.component;

import java.util.List;

import root.model.action.attack.AttackType;
import root.model.action.move.MoveType;
import root.model.event.ModelEventHandler;

public interface Unit extends Cloneable {

	// TODO should this be enum ... ? 
	// unit type ... basic, advanced, archers...
	String getUnitName();

	Field getField();

	void setField(Field field);

	boolean canMove();

	MoveType getMoveType();

	void relocateTo(Field nextField);

	List<AttackType> getAttacks();

	boolean hasAttack();

	Unit clone() throws CloneNotSupportedException;

	void setEventHandler(ModelEventHandler eventHandler);

}
