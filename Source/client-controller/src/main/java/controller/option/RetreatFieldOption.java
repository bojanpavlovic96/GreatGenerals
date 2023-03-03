package controller.option;

import java.util.Collections;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;

public class RetreatFieldOption extends FieldOption {

	public static final String name = "retreat-field-option";

	public RetreatFieldOption(Controller gameController) {
		super(RetreatFieldOption.name, gameController);
	}

	@Override
	public void run() {
		var unit = getPrimaryField().getUnit();
		var model = controller.getModel();

		var retreatDest = model
				.getFreeNeighbours(getPrimaryField(), 1)
				.stream()
				.map(this::mapToDistField)
				.sorted(Collections.reverseOrder())
				.map(this::mapToJustField)
				.findFirst();

		if (!retreatDest.isPresent()) {
			System.out.println("No more fields to retreat, you are doomed ... ");
			return;
		}

		// TODO 
		// Why can't field just stop attack without running away and then once the 
		// attack is stopped select moveFieldOption in order to move. 
		// This will be handy for tower implementation since retreat event 
		// won't involve any movement, it will be more simple (as it should be ... ?). 
		unit.getMove().calculatePath(model, getSecondaryField(), retreatDest.get());

	}

	private FieldWithDist mapToDistField(Field field) {
		var model = controller.getModel();
		var opponent = getPrimaryField().getUnit().getActiveAttack().getTarget();

		return new FieldWithDist(field, model.distance(field, opponent));
	}

	private Field mapToJustField(FieldWithDist distF) {
		return distF.field;
	}

	@Override
	public FieldOption getCopy() {
		return new RetreatFieldOption(controller);
	}

	@Override
	public boolean isAdequateFor(Field selectedField, Field targetField) {
		return (selectedField.getUnit() != null &&
				controller.isOwner(selectedField.getUnit().getOwner().getUsername()) &&
				selectedField.getUnit().isAttacking() &&
				selectedField.getUnit().getMove() != null);
	}

}
