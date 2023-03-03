package controller.option;

import java.util.stream.Collectors;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ShowSubmenuCommand;

public class AttacksSubmenuFieldOption extends FieldOption {

	public static final String Name = "attacks-submenu-field-option";

	public AttacksSubmenuFieldOption(Controller gameController) {
		super(AttacksSubmenuFieldOption.Name, gameController);
	}

	@Override
	public void run() {
		var options = getPrimaryField()
				.getUnit()
				.getAttacks()
				.stream()
				.map((attack) -> (FieldOption) new AttackFieldOption(controller, attack))
				.collect(Collectors.toList());

		options.forEach((option) -> option.setSecondaryField(getSecondaryField()));

		var showSubmenu = new ShowSubmenuCommand(options);

		super.controller.getConsumerQueue().enqueue(showSubmenu);

	}

	@Override
	public FieldOption getCopy() {
		return new AttacksSubmenuFieldOption(this.controller);
	}

	@Override
	public boolean isAdequateFor(Field selectedField, Field targetField) {
		var notTheSame = selectedField != targetField;
		var hasAttack = selectedField.getUnit() != null && selectedField.getUnit().hasAttacks();
		var hasOpponent = targetField.getUnit() != null;

		return (notTheSame &&
				hasAttack &&
				hasOpponent &&
				!controller.isOwner(secondaryField.getUnit().getOwner().getUsername()));
	}

}
