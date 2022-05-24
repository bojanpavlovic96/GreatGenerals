package model.component.unit;

import java.util.ArrayList;
import java.util.List;

import root.model.action.attack.AttackType;
import root.model.action.move.MoveType;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.event.ModelEventHandler;

public class BasicUnit implements Unit {

	// TODO should be enum ? 
	public static final String unitName = "basic-unit";

	private Field myField;

	private MoveType movementType;
	private List<AttackType> attacks;

	private ModelEventHandler eventHandler;

	public BasicUnit() {
		// attention may be used only in clone
		// protected is better solution than public
	}

	public BasicUnit(MoveType move, List<AttackType> attacks) {

		this.movementType = move;
		this.attacks = attacks;
	}

	public BasicUnit(MoveType move, AttackType attack) {

		this.movementType = move;
		this.attacks = new ArrayList<>();
		this.attacks.add(attack);
	}

	@Override
	public String getUnitName() {
		return BasicUnit.unitName;
	}

	@Override
	public Field getField() {
		return this.myField;
	}

	@Override
	public void setField(Field newField) {

		this.myField = newField;

		if (this.canMove()) {
			this.movementType.setField(newField);
		}

		// TODO do the same thing for attack

	}

	@Override
	public boolean canMove() {
		return this.movementType != null;
	}

	@Override
	public MoveType getMoveType() {
		return this.movementType;
	}

	@Override
	public List<AttackType> getAttacks() {
		return this.attacks;
	}

	@Override
	public boolean hasAttack() {
		return this.attacks != null && !this.attacks.isEmpty();
	}

	@Override
	public void relocateTo(Field nextField) {

		// remove from current field
		this.myField.setUnit(null);
		// set on next field
		nextField.setUnit(this);

		// update units reference to field
		// and also movement type reference
		this.setField(nextField);

	}

	@Override
	public Unit clone() throws CloneNotSupportedException {
		// exception just because... cloneable...

		BasicUnit clone = (BasicUnit) super.clone();

		clone.movementType = this.movementType.clone();
		clone.attacks = new ArrayList<>();
		for (AttackType attack : this.attacks) {
			clone.attacks.add(attack.clone());
		}

		return clone;
	}

	@Override
	public void setEventHandler(ModelEventHandler handler) {
		this.eventHandler = handler;

		if (this.canMove()) {
			this.movementType.setEventHandler(this.eventHandler);
		}

		// TODO do the same for attack

	}

}
