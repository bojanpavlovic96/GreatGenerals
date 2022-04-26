package root.model.component;

import root.model.action.move.MoveType;
import root.model.event.ModelEventHandler;

public interface Unit extends Cloneable {

	// attention may be unused
	String getUnitId();

	// TODO should this be enum ... ? 
	// unit type ... basic, advanced, archers...
	String getUnitName();

	Field getField();

	void setField(Field field);

	boolean canMove();

	// attention maybe list of move types
	MoveType getMoveType();

	void relocateTo(Field next_field);

	boolean haveAirAttack();

	boolean haveGroundAttack();

	Unit clone() throws CloneNotSupportedException;

	void setEventHandler(ModelEventHandler event_handler);

}
