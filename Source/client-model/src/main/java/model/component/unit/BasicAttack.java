package model.component.unit;

import root.model.action.attack.Attack;

public class BasicAttack extends Attack {

	public BasicAttack(String type, 
			int hitDamage,
			int range,
			long duration,
			long cooldown) {
		super(type, hitDamage, range, duration, cooldown);

	}

}
