package model.component.unit;

import root.model.action.attack.Attack;

public class BasicAttack extends Attack {

	public BasicAttack(int hitDamage,
			int range,
			long duration,
			long cooldown) {
		super(hitDamage, range, duration, cooldown);

	}

}
