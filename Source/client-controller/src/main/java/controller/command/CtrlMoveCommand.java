package controller.command;

import java.util.function.Function;

import root.Point2D;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.controller.Controller;
import root.model.component.Field;

import view.command.DrawFieldCommand;
import view.command.SelectFieldCommand;
import view.command.UnselectFieldCommand;

public class CtrlMoveCommand extends Command {

	public Point2D startFieldPos;
	public Field startField;

	public Point2D secondFieldPos;
	public Field secondField;

	public CtrlMoveCommand(Point2D firstPosition, Point2D secondPosition) {
		// super(CommandType.CtrlMove);

		this.startFieldPos = firstPosition;
		this.secondFieldPos = secondPosition;
	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.startField = ((Controller) super.targetComponent)
				.getModel()
				.getField(this.startFieldPos);
		this.secondField = ((Controller) super.targetComponent)
				.getModel()
				.getField(this.secondFieldPos);
	}

	@Override
	public void run() {

		var controller = (Controller) super.targetComponent;
		var viewCommandQueue = controller.getConsumerQueue();

		secondField.setUnit(startField.getUnit());
		startField.setUnit(null);

		var unselectFirst = new UnselectFieldCommand(this.startField);
		var redrawSecond = new DrawFieldCommand(this.secondField);

		viewCommandQueue.enqueue(unselectFirst);
		viewCommandQueue.enqueue(redrawSecond);

		if (startField == controller.getSelectedField()) {

			var selectSecond = new SelectFieldCommand(secondField);
			viewCommandQueue.enqueue(selectSecond);

			var undoStack = controller.getUndoStack();
			undoStack.removeFirstMatch(getCommandMatchLambda(this.startField));

			controller.setSelectedField(secondField);

		} else {
			// if one of the fields that are gonna be redrawn are on the selected
			// field's path (not the one currently moving) then select them
			var selField = controller.getSelectedField();

			if (selField != null &&
					selField.getUnit() != null &&
					selField.getUnit().getMove() != null &&
					controller.isOwner(selField.getUnit().getOwner().getUsername())) {

				if (selField.getUnit().getMove().isOnPath(startField)) {
					var selFirst = new SelectFieldCommand(startField);
					viewCommandQueue.enqueue(selFirst);
				}

				if (selField.getUnit().getMove().isOnPath(secondField)) {
					var selSecond = new SelectFieldCommand(secondField);
					viewCommandQueue.enqueue(selSecond);
				}

			}
		}

		// note, unit is now on secondField

		var unit = secondField.getUnit();

		if (controller.isOwner(unit.getOwner().getUsername())) {

			var unitPath = unit.getMove().getPath();

			unitPath.remove(0);

			if (unit.isAttacking()) {
				var destination = unit.getActiveAttack().getTarget();
				var distance = controller.getModel().distance(secondField, destination);

				if (distance <= unit.getActiveAttack().attackRange) {

					unit.getMove().stopMoving();
					unit.getMove().clearPath();

					// var attackIntention = new AttackModelEventArg(unit.getOwner().getUsername(),
					// secondField.getStoragePosition(),
					// destination.getStoragePosition());

					// controller.getServerProxy().sendIntention(attackIntention);

					System.out.println("Will attack: h: " + unit.getHealth() + " H: "
							+ unit.getActiveAttack().getTarget().getUnit().getHealth());

					unit.getActiveAttack().attack();

				} else {
					this.secondField.getUnit().getMove().move();
				}

			} else {
				if (!unitPath.isEmpty() && unitPath.size() > 1) {
					// continue moving

					// trigger timer
					this.secondField.getUnit().getMove().move();
				} else {
					this.secondField.getUnit().getMove().clearPath();
				}
			}
		} else {
			// follow the enemy

			// TODO this is a mess
			// AttackPath should not be calculated to the enemy unit, insted to the
			// closest field from which our unit can attack enemy (considering units's
			// range).
			// Look at the attackFieldOption logic.
			// For now it "chased" unit moves, just stop the attack ...
			// var attackingUnits = controller.getModel()
			// .getActiveUnits()
			// .stream()
			// .filter((actUnit) -> actUnit.isAttacking() &&
			// actUnit.getActiveAttack().getTarget() == startField)
			// .collect(Collectors.toList());

			controller.getModel()
					.getActiveUnits().stream()
					.filter((actUnit) -> actUnit.isAttacking() && actUnit.getActiveAttack().getTarget() == startField)
					.forEach((actUnit) -> {
						actUnit.getMove().stopMoving();

						actUnit.activateAttack(null);
						actUnit.getActiveAttack().setTarget(null);
					});

			// .collect(Collectors.toList());

			// for (var attacker : attackingUnits) {
			// var newPath = attacker.getMove().calculatePath(controller.getModel(),
			// attacker.getField(),
			// secondField);

			// attacker.getMove().clearPath();
			// attacker.getMove().addToPath(newPath);

			// attacker.getActiveAttack().setTarget(secondField);
			// }

		}

		return;
	}

	private Function<Command, Boolean> getCommandMatchLambda(Field targetField) {
		var targetPosition = targetField.getStoragePosition();

		return (Command currentCommand) -> {

			if (currentCommand instanceof SelectFieldCommand) {
				// if (currentCommand.getName().equals(SelectFieldCommand.commandName)) {
				var currentPos = ((SelectFieldCommand) currentCommand)
						.getField()
						.getStoragePosition();

				if (currentPos.getX() == targetPosition.getX()
						&& currentPos.getY() == targetPosition.getY()) {

					return true;
				}
			}

			return false;
		};
	}

	public Point2D getSecondPosition() {
		return secondFieldPos;
	}

	public void setSecondPosition(Point2D second_position) {
		this.secondFieldPos = second_position;
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
