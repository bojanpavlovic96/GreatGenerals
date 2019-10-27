package view.command;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;

public class SelectFieldCommand extends Command {

	// TODO filter color should be adjusted based on the player's color
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

	public SelectFieldCommand(Field modelField, CommandDrivenComponent target_component) {
		super("select-field-view-command", target_component);

		this.filter_color = SelectFieldCommand.default_filter_color;

		this.modelField = modelField;
		this.view_field = ((View) this.target_component).convertToViewField(this.modelField);

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.view_field = ((View) super.target_component).convertToViewField(this.modelField);

	}

	public void run() {

		GraphicsContext gc = ((View) target_component).getMainGraphicContext();

		((View) target_component).convertToViewField(modelField).paintField(gc, filter_color);

	}

	@Override
	public Command getAntiCommand() {
		return new UnselectFieldCommand(this.modelField);
	}

}
