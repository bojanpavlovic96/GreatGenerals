package view.command;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;

public class ComplexSelectFieldCommand extends Command {

	public static final String commandName = "select-field-view-command";

	private Color filter_color;

	private Field modelField;
	private ViewField viewField;

	// TODO this should be read from some manager or config
	private static Color default_filter_color = Color.rgb(100, 10, 10, 0.5);

	public ComplexSelectFieldCommand(Field modelField) {
		super(ComplexSelectFieldCommand.commandName);

		this.filter_color = ComplexSelectFieldCommand.default_filter_color;

		this.modelField = modelField;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.viewField = ((View) super.targetComponent)
				.convertToViewField(this.modelField);

	}

	public void run() {

		GraphicsContext gc = ((View) targetComponent).getMainGraphicContext();

		((View) targetComponent)
				.convertToViewField(modelField)
				.paintField(gc, filter_color);

		if (this.modelField.getUnit() != null
				&& this.modelField.getUnit().getMoveType().getPath() != null) {

			for (Field pathField : this.modelField.getUnit().getMoveType().getPath()) {

				((View) targetComponent)
						.convertToViewField(pathField)
						.paintField(gc, filter_color);

			}
		}

	}

	@Override
	public Command getAntiCommand() {
		return new ComplexUnselectFieldCommand(this.modelField);
	}

}
