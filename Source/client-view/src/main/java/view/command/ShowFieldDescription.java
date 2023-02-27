package view.command;

import root.Point2D;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;
import root.view.menu.FieldDescription;

public class ShowFieldDescription extends Command {

	private Field targetField;

	private ViewField viewTarget;

	private FieldDescription descMenu;

	public ShowFieldDescription(Field targetField) {
		this.targetField = targetField;
	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.targetComponent = target;

		viewTarget = ((View) target).convertToViewField(targetField);

		descMenu = ((View) target).getDescriptionMenu();

	}

	@Override
	public void run() {

		var view = (View) targetComponent;

		// System.out.println("Descriptions ... ");
		// for (var desc : fieldDesc) {
		// 	System.out.println(desc);
		// }
		descMenu.populateWith(viewTarget.getDescription());

		var optionsPos = view.getMainOptionsMenu().getPosition();
		var optionsWidth = view.getMainOptionsMenu().getMenuWidth();

		var descPosition = new Point2D(optionsPos.x + optionsWidth, optionsPos.y);
		descMenu.setPosition(descPosition);

		// System.out.println("Showing description menu .. ");
		descMenu.setVisible(true);

	}

	@Override
	public Command getAntiCommand() {
		return new ClearTopLayerCommand();
	}

}
