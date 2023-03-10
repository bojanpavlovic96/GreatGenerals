package controller.option;

import model.intention.BuildIntention;
import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;

public class BuildUnitFieldOption extends FieldOption {

	private static final int REQUIRED_POINTS = 20;

	private String unitName;

	private int cost;

	public BuildUnitFieldOption(String unitName, int cost, Controller gameController) {
		super(unitName, gameController);

		this.unitName = unitName;
		this.cost = cost;
	}

	@Override
	public void run() {
		System.out.println("Building unit: " + unitName + " ... ");

		var user = controller.getModel().getOwner().getUsername();
		var intention = new BuildIntention(user,
				getSecondaryField().getStoragePosition(),
				unitName,
				cost);

		controller.getServerProxy().sendIntention(intention);
	}

	@Override
	public FieldOption getCopy() {
		return new BuildUnitFieldOption(unitName, cost, controller);
	}

	@Override
	public boolean isAdequateFor(Field selectedField, Field targetField) {
		return (controller.isOwner(targetField.getPlayer().getUsername())
				&& targetField.getPlayer().getPoints() >= REQUIRED_POINTS);
	}

}
