package controller.command;

import root.Point2D;
import root.command.Command;
import root.controller.Controller;
import root.model.component.UnitType;
import view.command.ClearFieldCommand;
import view.command.DrawFieldCommand;
import view.command.UpdatePointsCommand;

public class CtrlBuildUnitCommand extends Command {

	private String ownerName;
	private Point2D position;
	private String unitType;
	private int cost;

	public CtrlBuildUnitCommand(Point2D field, String unitType, String ownerName, int cost) {
		this.ownerName = ownerName;
		this.position = field;
		this.unitType = unitType;
		this.cost = cost;
	}

	@Override
	public void run() {
		var controller = (Controller) targetComponent;

		var enumType = UnitType.valueOf(unitType);
		var owner = controller.getModel().getPlayer(ownerName);

		var unit = controller.getModel().generateUnit(enumType, owner);

		var field = controller.getModel().getField(position);
		field.setUnit(unit);

		owner.removePoints(cost);

		var clearCommand = new ClearFieldCommand(field);
		var redrawCommand = new DrawFieldCommand(field);

		controller.getConsumerQueue().enqueue(clearCommand);
		controller.getConsumerQueue().enqueue(redrawCommand);

		if (controller.isOwner(owner.getUsername())) {
			var updatePoints = new UpdatePointsCommand(owner.getPoints());
			controller.getConsumerQueue().enqueue(updatePoints);
		}
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
