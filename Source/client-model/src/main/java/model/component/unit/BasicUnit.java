package model.component.unit;

import root.model.action.move.MoveType;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.event.ModelEventHandler;

public class BasicUnit implements Unit {

	private Field my_field;

	private final String unit_name = "basic-unit";

	private String unit_id;

	// attention this should be list of attacks and movement types

	private MoveType movement_type;
	// attention air and ground attack could be the same thing with different range
	private UnitAttack air_attack;
	private UnitAttack ground_attack;

	private ModelEventHandler event_handler;

	// methods

	public BasicUnit() {
		// attention may be used only in clone
		// protected is better solution than public
	}

	public BasicUnit(MoveType move_ctrl, UnitAttack air_attack_ctrl, UnitAttack ground_attack_ctrl) {

		this.movement_type = move_ctrl;
		this.air_attack = air_attack_ctrl;
		this.ground_attack = ground_attack_ctrl;

	}

	public String getUnitId() {
		return this.unit_id;
	}

	public String getUnitName() {
		return this.unit_name;
	}

	public boolean canMove() {
		return this.movement_type != null;
	}

	public MoveType getMoveType() {
		return this.movement_type;
	}

	public boolean haveAirAttack() {
		return this.air_attack != null;
	}

	public boolean haveGroundAttack() {
		return this.ground_attack != null;
	}

	public Unit clone() throws CloneNotSupportedException {
		// exception just because... cloneable...

		BasicUnit clone = (BasicUnit) super.clone();

		clone.movement_type = this.movement_type.clone();

		/*
		 * to do same for air and ground attack
		 */

		return clone;
	}

	public void reallocateTo(Field next_field) {

		// remove from current field
		this.my_field.setUnit(null);
		// set on next field
		next_field.setUnit(this);
		
		// update units reference to field
		// and also movement type reference
		this.setField(next_field);

	}

	public void setEventHandler(ModelEventHandler handler) {
		this.event_handler = handler;

		if (this.canMove()) {
			this.movement_type.setEventHandler(this.event_handler);
		}

		// TODO do the same for attack

	}

	public Field getField() {
		return this.my_field;
	}

	public void setField(Field field) {

		this.my_field = field;

		if (this.canMove()) {
			this.movement_type.setField(field);
		}

		// TODO do the same thing for attack

	}

}
