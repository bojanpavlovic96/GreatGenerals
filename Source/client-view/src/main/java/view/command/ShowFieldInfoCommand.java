package view.command;

import java.util.List;

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

	private Field selected_field;
	private ViewField view_field;

	private Field target_field;
	private ViewField view_target;

	public ShowFieldInfoCommand(Field selected_field, Field target_field) {
		super("display-field-info-view-command");

		this.selected_field = selected_field;
		this.target_field = target_field;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.view_field = ((View) super.target_component).convertToViewField(this.selected_field);
		this.view_target = ((View) super.target_component).convertToViewField(this.target_field);

	}

	@Override
	public void run() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				// get view menu
				Menu menu = ((View) target_component).getOptionMenu();

				// clear previous options
				menu.clearOptions();

				// disable some options
				selected_field.adjustOptionsFor(target_field);

				// populate menu with new option
				List<FieldOption> options = selected_field.getEnabledOptions();
				for (int i = 0; i < options.size(); i++) {

					FieldOption item = options.get(i);

					item.setPrimaryField(selected_field);
					item.setSecondaryField(target_field);

					menu.addOption(new Option(item));

				}

				// set menu position
				((View) target_component).setMenuPosition(new Point2D(	view_target.getFieldCenter().getX(),
																		view_target.getFieldCenter().getY()));

				// show menu
				((View) target_component).setMenuVisibility(true);

				// draw field info

				GraphicsContext gc = ((View) target_component).getTopLayerGraphicContext();
				gc.save();

				gc.setFill(Color.GRAY);
				gc.fillRect(view_target.getFieldCenter().getX() + ((OptionMenu) menu).getWidth(),
							view_target.getFieldCenter().getY(),
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
