package model.component.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import root.communication.messages.components.AttackDesc;
import root.communication.messages.components.MoveDesc;
import root.communication.messages.components.UnitDesc;
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
		for (var attack : attacksDesc) {
			attacks.put(attack.type, attack);
		}

		this.units = new HashMap<String, UnitDesc>();
		for (var unit : unitsDesc) {
			units.put(unit.unitName, unit);
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
		if (desc != null) {

			return new BasicAttack(desc.damage,
					desc.range,
					desc.duration,
					desc.cooldown);

		} else {
			return null;
		}
	}

	public Unit generateUnit(UnitType type) {

		UnitDesc unitDesc = units.get(type.toString());

		Move move = genMove(unitDesc.moveType);

		var attacks = new ArrayList<Attack>();
		for (var attackType : unitDesc.attacks) {
			attacks.add(genAttack(attackType));
		}

		return new BasicUnit(UnitType.valueOf(unitDesc.unitName),
				move,
				attacks);
	}

}
