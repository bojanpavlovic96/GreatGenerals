package controller.command;

import java.util.List;
import java.util.function.Function;

import javafx.geometry.Point2D;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.command.CommandQueue;
import root.controller.Controller;
import root.model.component.Field;
import view.command.DrawFieldCommand;
import view.command.SelectFieldCommand;
import view.command.UnselectFieldCommand;

public class CtrlMoveCommand extends Command {

	public static final String name = "move-command";

	// TODO switch to root...Point2D
	public Field startField;

	public Point2D secondFieldPos;
	public Field secondField;

	public CtrlMoveCommand(Point2D firstPosition, Point2D secondPosition) {
		super(CtrlMoveCommand.name);

		this.startFieldPos = firstPosition;
		this.secondFieldPos = secondPosition;
	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.startField = ((Controller) super.targetComponent).getModel().getField(this.startFieldPos);
		this.secondField = ((Controller) super.targetComponent).getModel().getField(this.secondFieldPos);
	}

	@Override
	public void run() {

		Controller controller = (Controller) super.targetComponent;
		CommandQueue viewCommandQueue = controller.getConsumerQueue();

		this.startField.getUnit().relocateTo(this.secondField);

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
			// if startField is on currently selected field's path select startField
			var selField = controller.getSelectedField();
			if (selField.getUnit() != null
					&& selField.getUnit().getMoveType() != null) {

				if (selField.getUnit().getMoveType().isOnPath(this.startField)) {
					var selFirst = new SelectFieldCommand(this.startField);
					viewCommandQueue.enqueue(selFirst);
				}

				if (selField.getUnit().getMoveType().isOnPath(this.secondField)) {
					var selSecond = new SelectFieldCommand(this.secondField);
					viewCommandQueue.enqueue(selSecond);
				}

			}
		}

		List<Field> unitPath = this.secondField.getUnit().getMoveType().getPath();

		unitPath.remove(0);

		// note: unit is now on second_field
		if (!unitPath.isEmpty()) {
			// continue moving

			// trigger timer
			this.secondField.getUnit().getMoveType().move();
		}

		return;

	}

	private Function<Command, Boolean> getCommandMatchLambda(Field targetField) {
		var targetPosition = targetField.getStoragePosition();

		return (Command currentCommand) -> {

			if (currentCommand.getName().equals(SelectFieldCommand.commandName)) {
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
		// TODO maybe move one field backward ...
		return null;
	}

}
