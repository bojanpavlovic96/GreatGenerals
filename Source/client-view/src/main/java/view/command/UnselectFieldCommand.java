package view.command;

import javafx.scene.canvas.GraphicsContext;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.field.ViewField;
import root.view.View;

public class UnselectFieldCommand extends Command {

	private Field modelField;
	private ViewField viewField;

	public UnselectFieldCommand(Field modelField) {

		this.modelField = modelField;
	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.viewField = ((View) super.targetComponent)
				.convertToViewField(this.modelField);

	}

	@Override
	public void run() {
		GraphicsContext gc = ((View) this.targetComponent)
				.getMainGraphicContext();

		this.viewField.clearField(gc);
		this.viewField.drawOn(gc);
	}

	@Override
	public Command getAntiCommand() {
		return new SelectFieldCommand(this.modelField);
	}

}
