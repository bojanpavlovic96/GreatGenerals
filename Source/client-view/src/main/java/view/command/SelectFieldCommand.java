package view.command;

import javax.swing.DebugGraphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;

public class SelectFieldCommand extends Command {

	private Color filter_color;

	private Field modelField;
	private ViewField view_field;

	// attention extract from configuration
	private static Color default_filter_color = Color.rgb(100, 10, 10, 0.5);

	public SelectFieldCommand(Field modelField) {
		super("select-field-view-command");

		this.filter_color = SelectFieldCommand.default_filter_color;

		this.modelField = modelField;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.view_field = ((View) super.targetComponent).convertToViewField(this.modelField);

	}

	public void run() {

		GraphicsContext gc = ((View) targetComponent).getMainGraphicContext();

		gc.save();

		((View) targetComponent).convertToViewField(modelField).paintField(gc, filter_color);

		if (this.modelField.getUnit() != null &&
				this.modelField.getUnit().getMoveType().getPath() != null) {

			for (Field pathField : this.modelField.getUnit().getMoveType().getPath()) {

				System.out.println("Selecting path field ... ");
				((View) targetComponent).convertToViewField(pathField).paintField(gc, filter_color);

			}
		}

		gc.restore();

	}

	@Override
	public Command getAntiCommand() {
		return new UnselectFieldCommand(this.modelField);
	}

}
