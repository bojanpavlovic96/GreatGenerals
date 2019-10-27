package view.command;

import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.component.option.FieldOption;
import root.view.View;
import root.view.field.ViewField;
import root.view.menu.Menu;
import view.component.menu.Option;
import view.component.menu.OptionMenu;

public class ShowFieldOptionsCommand extends Command {

	public static double INFO_WIDTH = 200;
	public static double INFO_HEIGHT = 100;

	private Field selected_m_field;
	private ViewField selected_v_field;
	private Unit selected_unit;

	private Field target_m_field;
	private ViewField target_v_field;
	private Unit target_unit;

	public ShowFieldOptionsCommand(Field selected_field, Field target_field) {
		super("display-field-info-view-command");
		// attention this is not the most appropriate name

		this.selected_m_field = selected_field;
		this.target_m_field = target_field;

	}

	// unit can be extracted from selected or target field but if unit is moving extracting unit in
	// constructor can be late (unit is not on same field anymore), to prevent this case unit is passed
	// immediately
	public ShowFieldOptionsCommand(Field selectedField, Unit selectedUnit, Field targetField,
			Unit targetUnit) {
		this(selectedField, targetField);

		selected_unit = selectedUnit;
		target_unit = targetUnit;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		// TODO
		// check if selected field is null
		// if is null, target field is also selected field
		// update: ^ this is not ok when u actually think about it

		this.selected_v_field = ((View) super.target_component).convertToViewField(this.selected_m_field);
		this.target_v_field = ((View) super.target_component).convertToViewField(this.target_m_field);

	}

	@Override
	public void run() {

		Menu menu = ((View) target_component).getOptionMenu();
		menu.clearOptions();

		// disable some options
		selected_m_field.adjustOptionsFor(target_m_field);

		// populate menu with new option

		for (FieldOption singleOption : selected_m_field.getEnabledOptions()) {

			// singleOption.setSecondaryField(target_m_field); already done in adjustOptionsFor ...
			menu.addOption(new Option(singleOption));

		}

		((View) target_component).setMenuPosition(new Point2D(target_v_field.getFieldCenter().getX(),
				target_v_field.getFieldCenter().getY()));
		((View) target_component).setMenuVisibility(true);

		// draw field info
		GraphicsContext gc = ((View) target_component).getTopLayerGraphicContext();
		gc.save();

		gc.setFill(Color.GRAY);
		gc.fillRect(target_v_field.getFieldCenter().getX() + ((OptionMenu) menu).getWidth(),
					target_v_field.getFieldCenter().getY(),
					200,
					ShowFieldOptionsCommand.INFO_HEIGHT);

		gc.restore();

	}

	@Override
	public Command getAntiCommand() {
		return new ClearTopLayerCommand();
	}

}
