package controller.command;

import java.util.function.Function;

import root.Point2D;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.command.CommandQueue;
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
		var  viewCommandQueue = controller.getConsumerQueue();

		this.secondField.setUnit(this.startField.getUnit());
		this.startField.setUnit(null);

		var unselectFirst = new UnselectFieldCommand(this.startField);
		var redrawSecond = new DrawFieldCommand(this.secondField);

		viewCommandQueue.enqueue(unselectFirst);
		viewCommandQueue.enqueue(redrawSecond);

		if (this.startField == controller.getSelectedField()) {

			var selectSecond = new SelectFieldCommand(this.secondField);
			viewCommandQueue.enqueue(selectSecond);

			var undoStack = controller.getUndoStack();
			undoStack.removeFirstMatch(getCommandMatchLambda(this.startField));

			controller.setSelectedField(secondField);

		} else {
			// if startField is on currently selected field's path THEN select startField
			var selField = controller.getSelectedField();
			if (selField.getUnit() != null && selField.getUnit().getMove() != null) {

				if (selField.getUnit().getMove().isOnPath(this.startField)) {
					var selFirst = new SelectFieldCommand(this.startField);
					viewCommandQueue.enqueue(selFirst);
				}

				if (selField.getUnit().getMove().isOnPath(this.secondField)) {
					var selSecond = new SelectFieldCommand(this.secondField);
					viewCommandQueue.enqueue(selSecond);
				}

			} else {
				System.out.println("Received moveCommand for field without the unit or a move type ... ");
				System.out.println("Field: " + selField.getStoragePosition());
			}
		}

		var unitPath = this.secondField.getUnit().getMove().getPath();

		unitPath.remove(0);

		// note: unit is now on secondField
		if (!unitPath.isEmpty() && unitPath.size() > 1) {
			// continue moving

			// trigger timer
			this.secondField.getUnit().getMove().move();
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
