package model.component.unit;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import model.intention.AttackIntention;
import model.intention.DefendIntention;
import root.model.action.attack.Attack;
import root.model.event.Timer;

public class BasicAttack extends Attack {

	private Timer timer;

	private ScheduledFuture<?> attackFeature;

	public BasicAttack(String type,
			int attackDmg,
			long attackCooldown,
			int attackRange,
			int defenseDmg,
			long defenseCooldown,
			int defenseRange,
			long duration,
			Timer timer) {
		super(type,
				attackDmg,
				attackCooldown,
				attackRange,
				defenseDmg,
				defenseCooldown,
				defenseRange,
				duration);

		this.timer = timer;
	}

	@Override
	public void attack() {
		if (target == null || target.getUnit() == null) {
			System.err.println("Attack called without propper targer ... ");
			attackFeature = null;
			return;
		}

		attackFeature = timer.schedule(this::attackHandler, attackCooldown, TimeUnit.MILLISECONDS);
	}

	private void attackHandler() {
		System.out.println("Intention to attack ... ");

		var username = attacker.getOwner().getUsername();
		var attackerField = attacker.getField().getStoragePosition();
		var targetField = target.getStoragePosition();

		onEvent.handle(new AttackIntention(this.type, username, attackerField, targetField));
	}

	@Override
	public void defend() {
		if (target == null || target.getUnit() == null) {
			System.err.println("Defend called without propper targer ... ");
			attackFeature = null;
			return;
		}

		attackFeature = timer.schedule(this::defendHandler, defenseCooldown, TimeUnit.MILLISECONDS);
	}

	public void defendHandler() {
		System.out.println("Intention to defend ... ");

		var username = attacker.getOwner().getUsername();
		var attackerField = attacker.getField().getStoragePosition();
		var targetField = target.getStoragePosition();

		var event = new DefendIntention(this.type, username, attackerField, targetField);

		onEvent.handle(event);
	}

	@Override
	public void stopAttack() {
		if (attackFeature != null) {
			attackFeature.cancel(false);
		}
		attackFeature = null;
		target = null;
	}

}
