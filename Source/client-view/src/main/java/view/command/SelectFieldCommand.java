package view.command;

import javafx.scene.canvas.GraphicsContext;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.field.ViewField;
import root.view.Color;
import root.view.View;

public class SelectFieldCommand extends Command {

	private Color filterColor;

	private Field modelField;

	// TODO this should be read from some manager or config
	private static Color defaultFilterColor = Color.rgb(1, 0, 0, 0.3);

	public SelectFieldCommand(Field modelField, Color highlightColor) {

		this.modelField = modelField;
		this.filterColor = highlightColor;
	}

	public SelectFieldCommand(Field modelField) {
		this(modelField, SelectFieldCommand.defaultFilterColor);
	}

	@Override
	public void run() {
		GraphicsContext gc = ((View) targetComponent).getMainGraphicContext();

		((View) targetComponent)
				.convertToViewField(modelField)
				.paintField(gc, filterColor);

	}

	@Override
	public Command getAntiCommand() {
		return new UnselectFieldCommand(this.modelField);
	}

	public Field getField() {
		return this.modelField;
	}

}
