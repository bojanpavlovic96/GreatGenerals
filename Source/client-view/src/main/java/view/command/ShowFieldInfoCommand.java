package view.command;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import root.view.View;
import root.view.field.ViewField;
import root.view.menu.Menu;
import view.component.menu.Option;
import view.component.menu.OptionMenu;

public class ShowFieldInfoCommand extends Command {

	public static double INFO_WIDTH = 200;
	public static double INFO_HEIGHT = 100;

	private Field field_model;
	private ViewField view_field;

	public ShowFieldInfoCommand(Field model) {
		super("display-field-info-view-command");

		this.field_model = model;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.view_field = ((View) super.target_component).convertToViewField(this.field_model);

	}

	@Override
	public void run() {

		// get view menu
		Menu menu = ((View) super.target_component).getOptionMenu();

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				// clear previous options
				menu.clearOptions();

				// populate menu with new option
				for (FieldOption item : field_model.getOptions().values()) {
					menu.addOption(new Option(item.getName()));
				}

				((View) target_component).setMenuPosition(new Point2D(	view_field.getFieldCenter().getX(),
																		view_field.getFieldCenter().getY()));

				((View) target_component).setMenuVisibility(true);
				
				// draw field info
				
				GraphicsContext gc = ((View) target_component).getTopLayerGraphicContext();
				gc.save();

				gc.setFill(Color.GRAY);
				gc.fillRect(view_field.getFieldCenter().getX() + ((OptionMenu) menu).getWidth(),
							view_field.getFieldCenter().getY(),
							200,
							ShowFieldInfoCommand.INFO_HEIGHT);

				gc.restore();

			}
		});
	}

	@Override
	public Command getAntiCommand() {
		return new ClearTopLayerCommand();
	}

}
