package model.component.unit;

import model.component.Field;

public class BasicUnit implements Unit {

	private static String unit_name = "basic-unit";

	private String unit_id;

	private MoveType movement_type;

	private UnitAttack air_attack;

	private UnitAttack ground_attack;

	public BasicUnit(Field my_field) {

		this.movement_type = new BasicMove(my_field);

	}

	public String getUnitId() {
		return this.unit_id;
	}

	public String getUnitName() {
		return BasicUnit.unit_name;
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

}
