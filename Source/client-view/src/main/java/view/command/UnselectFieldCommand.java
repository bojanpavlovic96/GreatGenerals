package view.command;

import javafx.scene.canvas.GraphicsContext;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;

public class UnselectFieldCommand extends Command {

	private Field model_field;

	private ViewField view_field;

	public UnselectFieldCommand(Field model) {
		super("unselect-field-view-command");

		this.model_field = model;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.view_field = ((View) super.targetComponent).convertToViewField(this.model_field);

	}

	public void run() {

		GraphicsContext gc = ((View) this.targetComponent).getMainGraphicContext();

		this.view_field.clearField(gc);
		this.view_field.drawOn(gc);

		if (this.model_field.getUnit() != null
				&& this.model_field.getUnit().getMoveType().getPath() != null) {

			for (Field pathField : this.model_field.getUnit().getMoveType().getPath()) {

				ViewField viewField = ((View) this.targetComponent).convertToViewField(pathField);

				viewField.clearField(gc);
				viewField.drawOn(gc);

			}

		}

	}

	@Override
	public Command getAntiCommand() {
		return new SelectFieldCommand(this.model_field);
	}

}
