package controller.option;

import model.event.AttackModelEventArg;
import root.controller.Controller;
import root.model.action.attack.Attack;
import root.model.component.Field;
import root.model.component.option.FieldOption;

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
		if (distance <= attack.range) {
			if (unit.getMove() != null && unit.getMove().isMoving()) {
				unit.getMove().stopMoving();
			}

			var intention = new AttackModelEventArg(unit.getOwner().getUsername(),
					getPrimaryField().getStoragePosition(),
					getSecondaryField().getStoragePosition());

			controller.getServerProxy().sendIntention(intention);

			attack.setTarget(secondaryField);
			unit.activateAttack(attack);

		} else if (unit.getMove() != null) {

			if (unit.getMove().isMoving()) {
				unit.getMove().stopMoving();
			}

			unit.getMove().clearPath();

			var newPath = unit.getMove().calculatePath(controller.getModel(),
					getPrimaryField(),
					getSecondaryField());

			unit.getMove().addToPath(newPath);

			if (!unit.getMove().isMoving()) {
				unit.getMove().move();
			}

			attack.setTarget(secondaryField);
			unit.activateAttack(attack);

		} else {
			// unit can't move and is not in range ... 
			// just display some message 
			System.out.println("Enemy is outside the attack's range ... ");
		}

	}

	@Override
	public FieldOption getCopy() {
		return new AttackFieldOption(controller, attack);
	}

	@Override
	public boolean isAdequateFor(Field field) {
		return (field.getUnit() != null && field.getUnit().hasAttack(name));
	}

}
