package controller.option;

import model.intention.AbortAttackIntention;
import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;

public class AbortAttackFieldOption extends FieldOption {

	public static final String name = "Abort attack";

	public AbortAttackFieldOption(Controller gameController) {
		super(AbortAttackFieldOption.name, gameController);
	}

	@Override
	public void run() {

		System.out.println("Abort attack field option ... ");

		var position = getPrimaryField().getStoragePosition();
		var user = getPrimaryField().getUnit().getOwner().getUsername();

		var intention = new AbortAttackIntention(position, user);
		controller.getServerProxy().sendIntention(intention);
	}

	@Override
	public FieldOption getCopy() {
		return new AbortAttackFieldOption(this.controller);
	}

	@Override
	public boolean isAdequateFor(Field selectedField, Field targetField) {
		return (selectedField.getUnit() != null &&
				controller.isOwner(selectedField.getUnit().getOwner().getUsername()) &&
				selectedField.getUnit().isAttacking());
	}

}
