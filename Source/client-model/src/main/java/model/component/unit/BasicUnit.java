package model.component.unit;

import java.util.ArrayList;
import java.util.List;

import root.model.PlayerData;
import root.model.action.attack.Attack;
import root.model.action.move.Move;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.component.UnitType;
import root.model.event.ModelEventHandler;
import root.model.event.ModelEventProducer;

public class BasicUnit implements Unit, ModelEventProducer {

	private PlayerData owner;

	private UnitType type;

	private Field myField;

	private Move moveType;
	private List<Attack> attacks;

	private ModelEventHandler eventHandler;

	public BasicUnit(PlayerData owner, UnitType type, Move move, List<Attack> attacks) {
		this.owner = owner;

		this.type = type;

		this.moveType = move;
		this.attacks = attacks;
	}

	public BasicUnit(UnitType type, Move move, Attack attack) {
		this.type = type;
		this.moveType = move;
		this.attacks = new ArrayList<>();
		this.attacks.add(attack);
	}

	@Override
	public UnitType getUnitType() {
		return this.type;
	}

	// @Override
	// public Field getField() {
	// 	return this.myField;
	// }

	// @Override
	// public void setField(Field newField) {

	// 	this.myField = newField;

	// }

	@Override
	public boolean canMove() {
		return this.moveType != null;
	}

	@Override
	public Move getMove() {
		return this.moveType;
	}

	@Override
	public List<Attack> getAttacks() {
		return this.attacks;
	}

	@Override
	public boolean hasAttack() {
		return this.attacks != null && !this.attacks.isEmpty();
	}

	// @Override
	// public void relocateTo(Field nextField) {

	// 	// remove from current field
	// 	this.myField.setUnit(null);
	// 	// set on next field
	// 	nextField.setUnit(this);

	// 	// update units reference to field
	// 	// and also movement type reference
	// 	this.setField(nextField);

	// }

	@Override
	public ModelEventHandler getModelEventHandler() {
		return eventHandler;
	}

	@Override
	public void setModelEventHandler(ModelEventHandler handler) {
		this.eventHandler = handler;

		if (moveType != null && moveType instanceof ModelEventProducer) {
			((ModelEventProducer) moveType).setModelEventHandler(this.eventHandler);
		}

		if (attacks != null && !attacks.isEmpty()) {
			for (var attack : attacks) {
				if (attack instanceof ModelEventProducer) {
					((ModelEventProducer) attack).setModelEventHandler(this.eventHandler);
				}
			}
		}
	}

	@Override
	public PlayerData getOwner() {
		return this.owner;
	}

}
