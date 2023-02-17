package root.model.action.attack;

import root.model.component.Field;
import root.model.component.Unit;
import root.model.event.ModelEventHandler;
import root.model.event.ModelEventProducer;

public abstract class Attack implements ModelEventProducer, Runnable {

	public String type;

	public int hitDamage;
	public int range;
	public long duration;
	public long cooldown;

	protected ModelEventHandler onEvent;

	protected Unit attacker;
	protected Field target;

	public Attack(String type, int hitDamage, int range, long duration, long cooldown) {
		this.type = type;

		this.hitDamage = hitDamage;
		this.range = range;
		this.duration = duration;
		this.cooldown = cooldown;
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

	public abstract void stopAttack();
}
