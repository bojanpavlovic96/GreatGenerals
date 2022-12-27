package controller.option;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearTopLayerCommand;
import view.command.SelectFieldCommand;
import view.command.UnselectFieldCommand;

public class SelectPathFieldOption extends FieldOption {

	public static final String Name = "select-path-field-option";

	public SelectPathFieldOption(Controller gameController) {
		super(SelectPathFieldOption.Name, gameController);
	}

	@Override
	public void run() {

		var primaryField = this.getPrimaryField();
		if (primaryField == null) {
			return;
		}

		var currentPath = primaryField.getUnit().getMove().getPath();

		if (currentPath != null && !currentPath.isEmpty()) {

			for (Field pathField : currentPath) {
				var unselectCommand = new UnselectFieldCommand(pathField);
				super.controller.getConsumerQueue().enqueue(unselectCommand);
			}

		}

		// Second field must be without unit in order to calculate path.
		// That should be checked in adjustOptions on click on the destination field.
		var path = primaryField
				.getUnit()
				.getMove()
				.calculatePath(controller.getModel(), primaryField, secondaryField);

		System.out.println("FromField: " + primaryField.getStoragePosition()
				+ "\tToField: " + secondaryField.getStoragePosition());

		path.add(0, primaryField);

		// yes path.size() not path.size()-1 ... why ... I gues why not ... 
		for (Field pathField : path.subList(1, path.size())) {

			var selectCommand = new SelectFieldCommand(pathField);
			super.controller.getConsumerQueue().enqueue(selectCommand);
			super.controller.getUndoStack().push(selectCommand);
		}

		super.controller.getConsumerQueue().enqueue(new ClearTopLayerCommand());

		var dist = controller.getModel().distance(getPrimaryField(), getSecondaryField());
		System.out.println("Select distance: " + dist);

	}

	@Override
	public FieldOption getCopy() {
		return new SelectPathFieldOption(this.controller);
	}

	@Override
	public boolean isAdequateFor(Field field) {
		System.out.println("Owner: " + controller.isOwner(field.getPlayer().getUsername()));
		System.out.println("Not same: " + (field != getSecondaryField()));
		System.out.println("Has unit: " + (field.getUnit() != null));
		if (field.getUnit() != null) {
			System.out.println("Can move: " + (field.getUnit().getMove() != null));
		}
		return (controller.isOwner(field.getPlayer().getUsername()) &&
				field != getSecondaryField() &&
				field.getUnit() != null &&
				field.getUnit().getMove() != null&&
				getSecondaryField().getUnit()==null);
	}

}
