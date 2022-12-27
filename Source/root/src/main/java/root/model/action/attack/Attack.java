package root.model.action.attack;

import root.model.component.Field;
import root.model.event.ModelEventHandler;
import root.model.event.ModelEventProducer;

public class Attack implements ModelEventProducer {

	public String type;

	public int hitDamage;
	public int range;
	public long duration;
	public long cooldown;

	private ModelEventHandler handler;

	private Field target;

	public Attack(String type, int hitDamage, int range, long duration, long cooldown) {
		this.type = type;

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

	public void setTarget(Field target){
		this.target = target;
	}

	public Field getTarget(){
		return this.target;
	}
}
