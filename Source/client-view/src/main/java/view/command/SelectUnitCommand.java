package view.command;

import java.util.List;

import root.command.Command;
import root.model.component.Field;
import root.model.component.Unit;

public class SelectUnitCommand extends Command {

	private Unit selectedUnit;

	public SelectUnitCommand(Unit selected_unit) {
		super("select-unit-field-command");

		selectedUnit = selected_unit;

	}

	@Override
	public void run() {

		List<Field> path = this.selectedUnit.getMoveType().getPath();

		Command selectCommand = null;
		selectCommand = new SelectFieldCommand(this.selectedUnit.getField(), this.target_component);
		selectCommand.run();

		if (path != null) {

			for (Field pathField : path) {

				selectCommand = new SelectFieldCommand(pathField, this.target_component);
				selectCommand.run();

			}

		}

		// TODO also it there is some info bar, show informations about unit ...

	}

	@Override
	public Command getAntiCommand() {
		return new UnselectUnitCommand(this.selectedUnit);
	}

}
