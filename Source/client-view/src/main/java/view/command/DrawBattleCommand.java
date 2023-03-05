package view.command;

import root.Point2D;
import root.command.Command;
import root.model.component.Field;
import root.view.View;
import view.ResourceManager;

public class DrawBattleCommand extends Command {

	private Field startField;
	private Field endField;

	public DrawBattleCommand(Field startField, Field endField) {
		this.startField = startField;
		this.endField = endField;
	}

	@Override
	public void run() {

		var view = (View) targetComponent;

		var viewSField = view.convertToViewField(startField);
		var viewEField = view.convertToViewField(endField);

		var arrow = ResourceManager.getInstance().getBattleArrow("");
		var angle = angle(viewSField.getFieldCenter(), viewEField.getFieldCenter());

		var gc = view.getMainGraphicContext();

		gc.save();

		var dist = distance(viewSField.getFieldCenter(), viewEField.getFieldCenter());

		System.out.println("\t Battle arrow: ");
		System.out.println("\t f: " + startField + " t: " + endField);
		System.out.println("\t from: " + viewSField.getFieldCenter() + " to: " + viewEField.getFieldCenter());
		System.out.println("\t angle: " + angle);
		System.out.println("\t dist: " + dist);

		gc.translate(viewSField.getFieldCenter().x, viewEField.getFieldCenter().y);
		gc.rotate(angle);

		var hOffset = view.getFieldWidth() / 2;
		var arrowLen = dist - 2 * hOffset;
		var arrowHeight = 0.8 * view.getFieldHeight();

		// gc.drawImage(arrow, hOffset, 0 - (arrowHeight / 2), arrowLen, arrowHeight);
		gc.drawImage(arrow, 0, 0 - (arrow.getHeight() / 2), dist, view.getFieldHeight());

		gc.restore();

	}

	private double distance(Point2D a, Point2D b) {
		double x_diff = Math.abs(a.x - b.x);
		double y_diff = Math.abs(a.y - b.y);
		return Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));
	}

	// TODO 2. and 3. quadrant !!!
	private double angle(Point2D a, Point2D b) {

		double x_diff = a.x - b.x;
		double y_diff = a.y - b.y;

		return Math.toDegrees(Math.atan(y_diff / x_diff));
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
