package model.component.unit;

import java.util.ArrayList;
import java.util.List;

import root.model.action.attack.Attack;
import root.model.action.move.Move;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.component.UnitType;
import root.model.event.ModelEventHandler;
import root.model.event.ModelEventProducer;

public class BasicUnit implements Unit, ModelEventProducer {

	// public static final String unitName = "basicunit";

	private UnitType type;

	private Field myField;

	private Move movementType;
	private List<Attack> attacks;

	private ModelEventHandler eventHandler;

	public BasicUnit(UnitType type, Move move, List<Attack> attacks) {
		this.type = type;
		this.movementType = move;
		this.attacks = attacks;
	}

	public BasicUnit(UnitType type, Move move, Attack attack) {
		this.type = type;
		this.movementType = move;
		this.attacks = new ArrayList<>();
		this.attacks.add(attack);
	}

	@Override
	public UnitType getUnitType() {
		return this.type;
	}

	@Override
	public Field getField() {
		return this.myField;
	}

	@Override
	public void setField(Field newField) {

		this.myField = newField;

		// TODO do the same thing for attack

	}

	@Override
	public boolean canMove() {
		return this.movementType != null;
	}

	@Override
	public Move getMoveType() {
		return this.movementType;
	}

	@Override
	public List<Attack> getAttacks() {
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

	// @Override
	// public Unit clone() throws CloneNotSupportedException {
	// 	// exception just because... cloneable...

	// 	BasicUnit clone = (BasicUnit) super.clone();

	// 	clone.movementType = this.movementType.clone();
	// 	clone.attacks = new ArrayList<>();
	// 	for (AttackType attack : this.attacks) {
	// 		clone.attacks.add(attack.clone());
	// 	}

	// 	return clone;
	// }

	@Override
	public void setEventHandler(ModelEventHandler handler) {
		this.eventHandler = handler;

		if (this.canMove()) {
			this.movementType.setModelEventHandler(this.eventHandler);
		}

		// TODO do the same for attack

	}

	@Override
	public ModelEventHandler getModelEventHandler() {
		return eventHandler;
	}

	@Override
	public void setModelEventHandler(ModelEventHandler handler) {
		this.eventHandler = handler;
	}

}
