package view.command;

import javafx.scene.canvas.GraphicsContext;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;

public class ComplexUnselectFieldCommand extends Command {

	private Field modelField;

	private ViewField viewField;

	public ComplexUnselectFieldCommand(Field model) {

		this.modelField = model;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.viewField = ((View) super.targetComponent).convertToViewField(this.modelField);

	}

	public void run() {

		GraphicsContext gc = ((View) this.targetComponent).getMainGraphicContext();

		this.viewField.clearField(gc);
		this.viewField.drawOn(gc);

		if (this.modelField.getUnit() != null
				&& this.modelField.getUnit().getMoveType().getPath() != null) {

			for (Field pathField : this.modelField.getUnit().getMoveType().getPath()) {

				ViewField viewField = ((View) this.targetComponent).convertToViewField(pathField);

				viewField.clearField(gc);
				viewField.drawOn(gc);

			}

		}

	}

	@Override
	public Command getAntiCommand() {
		return new ComplexSelectFieldCommand(this.modelField);
	}

}
