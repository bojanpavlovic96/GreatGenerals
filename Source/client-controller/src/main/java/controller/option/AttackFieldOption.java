package controller.option;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import root.controller.Controller;
import root.model.action.attack.Attack;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearTopLayerCommand;
import view.command.SelectFieldCommand;

public class AttackFieldOption extends FieldOption {

	public Attack attack;

	public AttackFieldOption(Controller gameController, Attack attack) {
		// TODO instead of just passing type which will represent attack name
		// maybe form some string with damage and range
		// or create custom FieldOptions which would have more data then
		// just a simple button with the label
		super(attack.type, gameController);

		this.attack = attack;
	}

	@Override
	public void run() {
		var unit = getPrimaryField().getUnit();

		var distance = controller.getModel().distance(getPrimaryField(), getSecondaryField());
		System.out.println("Calculated distance: " + distance);

		if (distance <= attack.attackRange) {
			if (unit.getMove() != null && unit.getMove().isMoving()) {
				unit.getMove().stopMoving();
			}

			// var intention = new AttackModelEventArg(unit.getOwner().getUsername(),
			// getPrimaryField().getStoragePosition(),
			// getSecondaryField().getStoragePosition());

			// controller.getServerProxy().sendIntention(intention);

			attack.setTarget(secondaryField);
			unit.activateAttack(attack);
			attack.attack();

			controller.getConsumerQueue().enqueue(new ClearTopLayerCommand());

		} else if (unit.getMove() != null) {

			if (unit.getMove().isMoving()) {
				unit.getMove().stopMoving();
			}

			unit.getMove().clearPath();

			System.out.println("Primary: " + getPrimaryField().getStoragePosition());
			System.out.println("Secondary: " + getSecondaryField().getStoragePosition());

			System.out.println("Looking for attack with range: " + attack.attackRange);

			var destinations = findDestinations(attack.attackRange);
			if (destinations.isEmpty()) {
				System.out.println("Chosen field cannot be attacked ... ");
				return;
			}

			List<Field> attackPath = null;
			var destIter = destinations.iterator();
			while (destIter.hasNext() && (attackPath == null || attackPath.isEmpty())) {
				var dest = destIter.next();
				attackPath = unit
						.getMove()
						.calculatePath(controller.getModel(), getPrimaryField(), dest);
			}

			if (attackPath == null || attackPath.isEmpty()) {
				System.out.println("No available paths for attack ... ");
				return;
			}

			System.out.println("Calculated path of length: " + attackPath.size());
			attackPath.add(0, getPrimaryField());
			// unit.getMove().addToPath(attackPath);
			// already done in calculate, but should not be

			attack.setTarget(secondaryField);

			unit.activateAttack(attack);

			unit.getMove().move();

			for (Field pathField : attackPath.subList(1, attackPath.size())) {

				var selectCommand = new SelectFieldCommand(pathField);
				super.controller.getConsumerQueue().enqueue(selectCommand);
				super.controller.getUndoStack().push(selectCommand);
			}

			controller.getConsumerQueue().enqueue(new ClearTopLayerCommand());

		} else {
			// unit can't move and is not in range ...
			// just display some message
			System.out.println("Enemy is outside the attack's range/abilities ... ");
		}

	}

	@Override
	public FieldOption getCopy() {
		return new AttackFieldOption(controller, attack);
	}

	@Override
	public boolean isAdequateFor(Field selectedField, Field targetField) {
		return (selectedField.getUnit() != null && selectedField.getUnit().hasAttack(name));
	}

	private List<Field> findDestinations(int maxRange) {
		var retList = new ArrayList<Field>();

		for (var range = 1; range <= maxRange; range++) {

			retList.addAll(controller
					.getModel()
					.getFreeNeighbours(getSecondaryField(), range));
		}

		return retList.stream()
				.map(this::distanceMap)
				.sorted()
				.map((df) -> df.field)
				.collect(Collectors.toList());

		// Assumption ... ^
		// If every field is traversed more than once inside the sort method
		// it would be less expensive to calculate distances for every field,
		// sort them and then map back to just Field type insted of calling
		// model.distance(f1,f2) for each comparison.
	}

	private FieldWithDist distanceMap(Field f) {
		return new FieldWithDist(f, controller.getModel().distance(getPrimaryField(), f));
	}

	// private class FieldDist implements Comparable<FieldDist> {
	// public Field field;
	// public int dist;

	// public FieldDist(Field field, int dist) {
	// this.field = field;
	// this.dist = dist;
	// }

	// @Override
	// public int compareTo(FieldDist f2) {
	// return ((Integer) dist).compareTo(f2.dist);
	// }
	// }

}
