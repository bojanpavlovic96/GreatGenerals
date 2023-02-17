package model.component.unit;

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

	private Move move;
	private List<Attack> attacks;

	private int health;

	private Attack activeAttack;

	private ModelEventHandler eventHandler;

	public BasicUnit(PlayerData owner,
			UnitType type,
			Move move,
			List<Attack> attacks,
			int health) {

		this.owner = owner;
		this.type = type;
		this.move = move;
		this.attacks = attacks;
		for (var attack : this.attacks) {
			attack.setAttacker(this);
		}
		this.health = health;
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
	}

	@Override
	public boolean canMove() {
		return this.move != null;
	}

	@Override
	public Move getMove() {
		return this.move;
	}

	@Override
	public List<Attack> getAttacks() {
		return this.attacks;
	}

	@Override
	public boolean hasAttacks() {
		return this.attacks != null && !this.attacks.isEmpty();
	}

	@Override
	public boolean hasAttack(String type) {
		for (var attack : attacks) {
			if (attack.type.equals(type)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ModelEventHandler getModelEventHandler() {
		return eventHandler;
	}

	@Override
	public void setModelEventHandler(ModelEventHandler handler) {
		this.eventHandler = handler;

		if (move != null && move instanceof ModelEventProducer) {
			((ModelEventProducer) move).setModelEventHandler(this.eventHandler);
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

	@Override
	public boolean isAttacking() {
		return activeAttack != null;
	}

	// Refactor to accept string (or enum) as the attack name 
	// and then search trough available attacks.
	// That way attack activation can be controller by the server. 
	@Override
	public void activateAttack(Attack attack) {
		this.activeAttack = attack;
	}

	@Override
	public void deactivateAttack() {
		if (this.activeAttack != null) {
			this.activeAttack.setTarget(null);
		}

		this.activeAttack = null;
	}

	@Override
	public Attack getActiveAttack() {
		return this.activeAttack;
	}

	@Override
	public int getHealth() {
		return this.health;
	}

	@Override
	public void attackWith(Attack attack) {
		health -= attack.hitDamage;
	}

	@Override
	public boolean isAlive() {
		return health > 0;
	}

	@Override
	public Attack getAttack(String requiredType) {
		for (var attack : this.attacks) {
			if (attack.type.equals(requiredType)) {
				return attack;
			}
		}

		return null;
	}

	@Override
	public void deactivate() {
		if (move != null && move.isMoving()) {
			move.stopMoving();
		}

		if (activeAttack != null) {
			activeAttack.stopAttack();
			activeAttack = null;
		}

	}

}
