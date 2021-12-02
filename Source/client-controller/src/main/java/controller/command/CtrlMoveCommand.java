package controller.command;

import java.util.List;

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

	private Point2D startFieldPos;
	private Field startField;

	private Point2D secondFieldPos;
	private Field secondField;

	public CtrlMoveCommand(Point2D firstPosition, Point2D secondPosition) {
		super("move-command");

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

		/**
		 * unselect firstField redraw secondField
		 * 
		 * if this.field is selected select secondField // path is still selected remove
		 * selectFirstField from undoStack else if has unit and unit has path if
		 * firstField on that path select first field
		 * 
		 */

		var unselectFirst = new UnselectFieldCommand(this.startField);
		var redrawSecond = new DrawFieldCommand(this.secondField);

		viewCommandQueue.enqueue(unselectFirst);
		viewCommandQueue.enqueue(redrawSecond);

		if (this.startField == controller.getSelectedField()) {
			var selectSecond = new SelectFieldCommand(this.secondField);
			viewCommandQueue.enqueue(selectSecond);

			// remove selectFirstField from undoStack
			var undoStack = controller.getUndoStack();
			int index = 0;
			Command targetCommand = null;
			var firstPosition = this.startField.getStoragePosition();
			while ((targetCommand = undoStack.get(index)) != null) {
				if (targetCommand.getName() == SelectFieldCommand.commandName) {
					var selCommand = (SelectFieldCommand) targetCommand;
					var selPosition = selCommand.getField().getStoragePosition();

					if (selPosition.getX() == firstPosition.getX()
							&& selPosition.getY() == firstPosition.getY()) {

						undoStack.remove(index);
						break; // break while loop
					}

				}
				index++;
			}

		} else {
			// if startField is on currently selected field's path select startField
			var selField = controller.getSelectedField();
			if (selField.getUnit() != null
					&& selField.getUnit().getMoveType() != null
					&& selField.getUnit().getMoveType().isOnPath(this.startField)) {

				var selFirst = new SelectFieldCommand(this.startField);
				viewCommandQueue.enqueue(selFirst);
			}
		}

		// viewCommandQueue.enqueue(new ComplexUnselectFieldCommand(this.startField));

		// // draw second field with the unit on it
		// viewCommandQueue.enqueue(new ClearFieldCommand(this.secondField));
		// viewCommandQueue.enqueue(new DrawFieldCommand(this.secondField));

		// if (this.startField == controller.getSelectedField()) {

		// viewCommandQueue.enqueue(new ComplexUnselectFieldCommand(this.secondField));
		// controller.selectField(secondField);

		// // viewCommandQueue.enqueue(new SelectFieldCommand(this.secondField));

		// // viewCommandQueue.enqueue(new ClearFieldCommand(this.startField));
		// // viewCommandQueue.enqueue(new DrawFieldCommand(this.startField));

		// // viewCommandQueue.enqueue(new ClearFieldCommand(this.secondField));
		// // viewCommandQueue.enqueue(new DrawFieldCommand(this.secondField));

		// }

		List<Field> unitPath = this.secondField.getUnit().getMoveType().getPath();

		unitPath.remove(0);

		// note: unit is now on second_field
		if (!unitPath.isEmpty()) {
			// continue moving

			// trigger timer
			this.secondField.getUnit().getMoveType().move();
		}

		return;
		// if (this.startField == controller.getSelectedField()) {
		// controller.selectField(secondField);
		// }

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
