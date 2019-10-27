package view.command;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import root.command.Command;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.component.option.FieldOption;
import root.view.View;
import root.view.menu.Menu;
import view.component.menu.Option;
import view.component.menu.OptionMenu;

public class ShowUnitOptionsCommand extends Command {

	private Unit selectedUnit;

	private Field targetField;
	private Unit targetUnit;

	public ShowUnitOptionsCommand(Unit selectedUnit, Field targetField, Unit targetUnit) {
		super("show-unit-options-view-command");

		this.selectedUnit = selectedUnit;
		this.targetField = targetField;
		this.targetUnit = targetUnit;
	}

	@Override
	public void run() {

		// check if target unit is null

		// also check if target field is null

		Menu menu = ((View) target_component).getOptionMenu();
		menu.clearOptions();

		this.selectedUnit.adjustOptionsFor(this.targetField, this.targetUnit);

		for (FieldOption singleOption : this.selectedUnit.getEnabledOptions()) {

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
