package view.command;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.model.component.Unit;
import root.view.field.ViewField;
import root.view.View;

public class SelectUnitPathCommand extends Command {

	// TODO this should be read from some manager or config
	private static final Color defaultHighlightColor = Color.rgb(100, 10, 10, 0.5);

	private Field modelField;
	private ViewField viewField;

	private Color highlightColor;

	// TODO this command might be useless
	public SelectUnitPathCommand(Field modelField) {

		this.modelField = modelField;
		this.highlightColor = SelectUnitPathCommand.defaultHighlightColor;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.viewField = ((View) super.targetComponent)
				.convertToViewField(this.modelField);
	}

	@Override
	public void run() {

		GraphicsContext gc = ((View) targetComponent).getMainGraphicContext();

		Unit unit = this.modelField.getUnit();
		if (unit != null
				&& unit.getMove() != null
				&& unit.getMove().getPath() != null) {

			for (Field pathField : this.modelField.getUnit().getMove().getPath()) {

				var selectFieldCommand = new SelectFieldCommand(pathField);
				selectFieldCommand.setTargetComponent(this.targetComponent);
				selectFieldCommand.run();

			}

		}

	}

	@Override
	public Command getAntiCommand() {
		return new UnselectUnitPath();
	}

}
