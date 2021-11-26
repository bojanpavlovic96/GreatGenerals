package view.command;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;
import root.view.menu.Menu;

public class ShowFieldInfoCommand extends Command {

	public static double INFO_WIDTH = 400;
	public static double INFO_HEIGHT = 200;

	private Field selectedField;
	// private ViewField viewField; // unused ? remove ?

	private Field targetField;
	private ViewField viewTarget;

	public ShowFieldInfoCommand(Field selected_field, Field target_field) {
		super("display-field-info-view-command");
		// attention this is not the most appropriate name

		this.selectedField = selected_field;
		this.targetField = target_field;
	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		// this.viewField = ((View)
		// super.targetComponent).convertToViewField(this.selectedField);
		this.viewTarget = ((View) super.targetComponent)
				.convertToViewField(this.targetField);

		if (this.selectedField == null) {
			this.selectedField = this.targetField;
		}

	}

	@Override
	public void run() {

		View view = (View) targetComponent;

		Menu menu = view.getOptionMenu();
		menu.clearOptions();

		selectedField.adjustOptionsFor(targetField);
		menu.populateWith(selectedField.getEnabledOptions());

		view.setMenuPosition(new Point2D(
				viewTarget.getFieldCenter().getX(),
				viewTarget.getFieldCenter().getY()));
		view.setMenuVisibility(true);

		// draw field info

		GraphicsContext gc = ((View) targetComponent).getTopLayerGraphicContext();
		gc.save();

		gc.setFill(Color.GRAY);

		gc.fillRect(viewTarget.getFieldCenter().getX() + menu.getMenuWidth(),
				viewTarget.getFieldCenter().getY(),
				ShowFieldInfoCommand.INFO_WIDTH,
				ShowFieldInfoCommand.INFO_HEIGHT);

		gc.restore();

	}

	@Override
	public Command getAntiCommand() {
		return new ClearTopLayerCommand();
	}

}
