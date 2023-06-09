package controller.option;

import model.intention.BuildIntention;
import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;

public class BuildUnitFieldOption extends FieldOption {

	private String unitName;
	private int cost;

	public BuildUnitFieldOption(String unitName, int cost, Controller gameController) {
		super(unitName + " = " + cost + " C", gameController);

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
				&& targetField.getPlayer().getCoins() >= 0);
		// TODO instead of 0 it should be compared with this.cost
		// that way all units bill be displayed and then server will 
		// decide if i am able to build one. 
		// If compared with cost available units have to be updated with every 
		// income tick. 
	}

}
