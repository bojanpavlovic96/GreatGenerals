package model.component.unit;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import model.event.AttackModelEventArg;
import model.event.DefendModelEventArg;
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
		super(type, attackDmg, attackCooldown, attackRange, defenseDmg, defenseCooldown, defenseRange, duration);

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

		attackFeature = timer.schedule(this::attackHandler, attackCooldown, TimeUnit.MILLISECONDS);
	}

	public void attackHandler() {
		System.out.println("Attack event raised ... ");

		var username = attacker.getOwner().getUsername();
		var attackerField = attacker.getField().getStoragePosition();
		var targetField = target.getStoragePosition();

		onEvent.handleModelEvent(new AttackModelEventArg(this.type, username, attackerField, targetField));
	}

	@Override
	public void defend() {
		if (target == null || target.getUnit() == null) {
			System.err.println("Defend called without propper targer ... ");
			attackFeature = null;
			return;
		}

		System.out.println("Intention to defend: "
				+ attacker.getField()
				+ " -> "
				+ target);

		attackFeature = timer.schedule(this::defendHandler, defenseCooldown, TimeUnit.MILLISECONDS);
	}

	public void defendHandler() {
		System.out.println("Defend event raised ... ");

		var username = attacker.getOwner().getUsername();
		var attackerField = attacker.getField().getStoragePosition();
		var targetField = target.getStoragePosition();

		System.out.println("All data gathered ... ");
		var event = new DefendModelEventArg(this.type, username, attackerField, targetField);
		System.out.println("Event created ... ");

		if (onEvent == null) {
			System.out.println("On event is null ... ");
		} else {
			System.out.println("On event is NOT null ... ");
		}

		onEvent.handleModelEvent(event);
	}

	@Override
	public void stopAttack() {
		if (attackFeature != null) {
			attackFeature.cancel(false);
		}
		attackFeature = null;
	}

}
