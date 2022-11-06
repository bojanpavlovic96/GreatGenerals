package view.command;

import javafx.scene.canvas.GraphicsContext;
import root.command.Command;
import root.model.component.Field;
import root.view.View;

public class SelectFieldCommand extends Command {

	private Field modelField;

	public SelectFieldCommand(Field modelField) {
		this.modelField = modelField;
	}

	@Override
	public void run() {
		GraphicsContext gc = ((View) targetComponent).getMainGraphicContext();

		var color = ((View) targetComponent).getActiveConfig().selectColor;

		((View) targetComponent)
				.convertToViewField(modelField)
				.paintField(gc, color);

	}

	@Override
	public Command getAntiCommand() {
		return new UnselectFieldCommand(this.modelField);
	}

	public Field getField() {
		return this.modelField;
	}

}
