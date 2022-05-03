package model.component.unit;

import root.model.action.move.MoveType;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.event.ModelEventHandler;

public class BasicUnit implements Unit {

	private Field myField;

	public static final String unitName = "basic-unit";

	private String unitId;

	// attention this should be list of attacks and movement types

	private MoveType movementType;
	// attention air and ground attack could be the same thing with different range
	private UnitAttack airAttack;
	private UnitAttack groundAttack;

	private ModelEventHandler eventHandler;

	// methods

	public BasicUnit() {
		// attention may be used only in clone
		// protected is better solution than public
	}

	public BasicUnit(MoveType moveCtrl,
			UnitAttack airAttackCtrl,
			UnitAttack groundAttackCtrl) {

		this.movementType = moveCtrl;
		this.airAttack = airAttackCtrl;
		this.groundAttack = groundAttackCtrl;
	}

	public String getUnitId() {
		return this.unitId;
	}

	public String getUnitName() {
		return BasicUnit.unitName;
	}

	public boolean canMove() {
		return this.movementType != null;
	}

	public MoveType getMoveType() {
		return this.movementType;
	}

	public boolean haveAirAttack() {
		return this.airAttack != null;
	}

	public boolean haveGroundAttack() {
		return this.groundAttack != null;
	}

	public Unit clone() throws CloneNotSupportedException {
		// exception just because... cloneable...

		BasicUnit clone = (BasicUnit) super.clone();

		clone.movementType = this.movementType.clone();

		/*
		 * TODO same for air and ground attack
		 */

		return clone;
	}

	public void relocateTo(Field nextField) {

		// remove from current field
		this.myField.setUnit(null);
		// set on next field
		nextField.setUnit(this);

		// update units reference to field
		// and also movement type reference
		this.setField(nextField);

	}

	public void setEventHandler(ModelEventHandler handler) {
		this.eventHandler = handler;

		if (this.canMove()) {
			this.movementType.setEventHandler(this.eventHandler);
		}

		// TODO do the same for attack

	}

	public Field getField() {
		return this.myField;
	}

	public void setField(Field newField) {

		this.myField = newField;

		if (this.canMove()) {
			this.movementType.setField(newField);
		}

		// TODO do the same thing for attack

	}

}
