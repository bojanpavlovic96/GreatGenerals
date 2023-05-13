package model.component.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import root.communication.messages.components.AttackDesc;
import root.communication.messages.components.MoveDesc;
import root.communication.messages.components.UnitDesc;
import root.model.PlayerData;
import root.model.action.attack.Attack;
import root.model.action.move.Move;
import root.model.action.move.PathFinder;
import root.model.component.Unit;
import root.model.component.UnitType;
import root.model.event.Timer;

public class UnitFactory {

	private Map<String, MoveDesc> moves;
	private Map<String, AttackDesc> attacks;

	private Map<String, UnitDesc> units;

	private PathFinder pathFinder;
	private Timer timer;

	public UnitFactory(List<UnitDesc> unitsDesc,
			List<MoveDesc> movesDesc,
			List<AttackDesc> attacksDesc,
			PathFinder pathFinder,
			Timer timer) {

		this.moves = new HashMap<>();
		for (var move : movesDesc) {
			moves.put(move.type, move);
		}

		this.attacks = new HashMap<>();
		System.out.println("Attack Descriptions ... ");
		for (var attack : attacksDesc) {
			attacks.put(attack.type, attack);
			System.out.println(attack);
		}

		this.units = new HashMap<String, UnitDesc>();
		System.out.println("Units desc ... ");
		for (var unit : unitsDesc) {
			units.put(unit.unitName, unit);
			System.out.println(unit);
		}

		this.pathFinder = pathFinder;
		this.timer = timer;

	}

	private Move genMove(String type) {
		var moveDesc = moves.get(type);
		if (moveDesc != null) {

			return new BasicMove(moveDesc.speed,
					moveDesc.terrainMultiplier,
					pathFinder,
					timer);
		} else {
			return null;
		}
	}

	private Attack genAttack(String type) {
		var desc = attacks.get(type);

		if (desc == null) {
			return null;
		}

		return new BasicAttack(type,
				desc.attackDmg,
				desc.attackCooldown,
				desc.attackRange,
				desc.defenseDmg,
				desc.defenseCooldown,
				desc.defenseRange,
				desc.duration,
				timer);

	}

	public Unit generateUnit(UnitType type, PlayerData owner) {

		UnitDesc unitDesc = units.get(type.toString());

		Move move = null;
		if (!unitDesc.moveType.equals("None")) {
			move = genMove(unitDesc.moveType);
		}
		// for (var att : unitDesc.attacks) {
		// 	System.out.println("Generating unit with att: " + att);
		// }
		var attacks = unitDesc.attacks.stream()
				.map((uDesc) -> genAttack(uDesc))
				.collect(Collectors.toList());

		var defense = genAttack(unitDesc.defense);

		return new BasicUnit(owner,
				UnitType.valueOf(unitDesc.unitName),
				move,
				attacks,
				defense,
				unitDesc.health);
	}

	public List<UnitDesc> getUnits() {
		return new ArrayList<UnitDesc>(units.values());
	}

}
