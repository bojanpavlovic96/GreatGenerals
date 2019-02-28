package model.component.unit;

import model.component.field.Field;

public class BasicUnit implements Unit {

	private final String unit_name = "basic-unit";

	private String unit_id;

	// attention this should be list of attacks

	private MoveType movement_type;

	private UnitAttack air_attack;

	private UnitAttack ground_attack;

	// methods

	public BasicUnit() {
		// may be used in clone
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

	public void moveTo(Field next_field) {

		next_field.setUnit(this);

		this.movement_type.getMyField().setUnit(null);
		this.movement_type.setMyField(next_field);

	}

}
