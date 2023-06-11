package controller.option;

import java.util.stream.Collectors;

import root.communication.messages.components.UnitDesc;
import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ShowSubmenuCommand;

public class ShowUnitsFieldOption extends FieldOption {

	public static final String NAME = "Build unit";

	public ShowUnitsFieldOption(Controller gameController) {
		super(NAME, gameController);
	}

	@Override
	public void run() {
		var units = controller.getModel().getUnitDescriptions();
		var options = units.stream()
				.map(this::optionFromUnit)
				.collect(Collectors.toList());

		options.forEach(option -> option.setSecondaryField(getSecondaryField()));

		var showSubmenuCommand = new ShowSubmenuCommand(options);
		controller.getConsumerQueue().enqueue(showSubmenuCommand);
	}

	private FieldOption optionFromUnit(UnitDesc desc) {
		return new BuildUnitFieldOption(desc.unitName, desc.cost,controller);
	}

	@Override
	public FieldOption getCopy() {
		return null;
	}

	@Override
	public boolean isAdequateFor(Field selectedField, Field targetField) {
		var isMine = controller.isOwner(targetField.getPlayer().getUsername());
		var isEmpty = targetField.getUnit() == null;
		return isMine && isEmpty;

	}

}
