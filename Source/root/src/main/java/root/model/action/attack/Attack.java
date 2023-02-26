package root.model.action.attack;

import root.model.component.Field;
import root.model.component.Unit;
import root.model.event.ModelEventHandler;
import root.model.event.ModelEventProducer;

public abstract class Attack implements ModelEventProducer {

	public String type;

	public int attackDmg;
	public long attackCooldown;
	public int attackRange;

	public int defenseDmg;
	public long defenseCooldown;
	public int defenseRange;

	// I guess will only be used with the animations ... 
	public long duration;

	protected ModelEventHandler onEvent;

	protected Unit attacker;
	protected Field target;

	public Attack(String type,
			int attackDmg,
			long attackCooldown,
			int attackRange,
			int defenseDmg,
			long defenseCooldown,
			int defenseRange,
			long duration) {

		this.type = type;
		this.attackDmg = attackDmg;
		this.attackCooldown = attackCooldown;
		this.attackRange = attackRange;
		this.defenseDmg = defenseDmg;
		this.defenseCooldown = defenseCooldown;
		this.defenseRange = defenseRange;
		this.duration = duration;
	}

	@Override
	public ModelEventHandler getModelEventHandler() {
		return this.onEvent;
	}

	@Override
	public void setModelEventHandler(ModelEventHandler handler) {
		this.onEvent = handler;

	}

	public void setAttacker(Unit attacker) {
		this.attacker = attacker;
	}

	public void setTarget(Field target) {
		this.target = target;
	}

	public Field getTarget() {
		return this.target;
	}

	public abstract void attack();

	public abstract void defend();

	// A bit misleading but this will stop the defense as well, 
	// since defense is basically attack just with different values ... 
	public abstract void stopAttack();
}
