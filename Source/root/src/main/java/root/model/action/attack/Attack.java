package root.model.action.attack;

import root.model.event.ModelEventHandler;
import root.model.event.ModelEventProducer;

public class Attack implements ModelEventProducer {

	public String type;

	public int hitDamage;
	public int range;
	public long duration;
	public long cooldown;

	private ModelEventHandler handler;

	public Attack(int hitDamage, int range, long duration, long cooldown) {
		this.hitDamage = hitDamage;
		this.range = range;
		this.duration = duration;
		this.cooldown = cooldown;
	}

	@Override
	public ModelEventHandler getModelEventHandler() {
		return this.handler;
	}

	@Override
	public void setModelEventHandler(ModelEventHandler handler) {
		this.handler = handler;

	}

}
