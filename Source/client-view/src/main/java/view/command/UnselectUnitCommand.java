package view.command;

import java.util.List;

import root.command.Command;
import root.model.component.Field;
import root.model.component.Unit;

public class UnselectUnitCommand extends Command {

	private Unit selectedUnit;

	public UnselectUnitCommand(Unit selected_unit) {
		super("unselect-unit-view-command");

		this.selectedUnit = selected_unit;

	}

	@Override
	public void run() {

		List<Field> path = this.selectedUnit.getMoveType().getPath();

		Command unselectCommand = null;
		unselectCommand = new UnselectFieldCommand(this.selectedUnit.getField(), this.target_component);
		unselectCommand.run();

		if (path != null) {

			for (Field pathField : path) {

				unselectCommand = new UnselectFieldCommand(pathField, this.target_component);
				unselectCommand.run();

			}

		}

		// TODO also it there is some info bar, hide informations about unit ...

	}

	@Override
	public Command getAntiCommand() {

		return new SelectUnitCommand(this.selectedUnit);

	}

}
