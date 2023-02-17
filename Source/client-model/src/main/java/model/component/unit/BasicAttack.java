package model.component.unit;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import model.event.AttackModelEventArg;
import root.model.action.attack.Attack;
import root.model.event.Timer;

public class BasicAttack extends Attack {

	private Timer timer;

	private ScheduledFuture<?> attackFeature;

	public BasicAttack(String type,
			int hitDamage,
			int range,
			long duration,
			long cooldown,
			Timer timer) {
		super(type, hitDamage, range, duration, cooldown);

		this.timer = timer;
	}

	@Override
	public void attack() {
		if (target == null || target.getUnit() == null) {
			System.err.println("Attack called without propper targer ... ");
			attackFeature = null;
			return;
		}

		System.out.println("Intention to attack: "
				+ attacker.getField()
				+ " -> "
				+ target);

		attackFeature = timer.schedule(this, cooldown, TimeUnit.MILLISECONDS);
	}

	@Override
	public void run() {
		System.out.println("Attack event raised ... ");

		var username = attacker.getOwner().getUsername();
		var attackerField = attacker.getField().getStoragePosition();
		var targetField = target.getStoragePosition();

		onEvent.handleModelEvent(new AttackModelEventArg(this.type, username, attackerField, targetField));
	}

	@Override
	public void stopAttack() {
		if (attackFeature != null) {
			attackFeature.cancel(false);
		}
		attackFeature = null;
	}

}
