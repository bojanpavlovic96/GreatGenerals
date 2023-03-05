package controller.command;

import root.Point2D;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.controller.Controller;
import root.model.component.Field;
import view.command.ClearBattleCommand;
import view.command.ClearFieldCommand;
import view.command.DrawBattleCommand;
import view.command.DrawFieldCommand;
import view.command.ShowFieldDescription;

public class CtrlAttackCommand extends Command {

	public String attackType;

	public Point2D startFieldPosition;
	public Point2D endFieldPosition;

	public Field startField;
	public Field endField;

	public CtrlAttackCommand(String attackType, Point2D startFieldPos, Point2D endFieldPos) {

		this.attackType = attackType;

		this.startFieldPosition = startFieldPos;
		this.endFieldPosition = endFieldPos;
	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.startField = ((Controller) super.targetComponent)
				.getModel()
				.getField(this.startFieldPosition);
		this.endField = ((Controller) super.targetComponent)
				.getModel()
				.getField(this.endFieldPosition);
	}

	@Override
	public void run() {
		var controller = (Controller) super.getTargetComponent();

		// drawing over with every attack ... 
		controller.getView()
				.getCommandQueue()
				.enqueue(new DrawBattleCommand(startField, endField));

		var attack = startField.getUnit().getAttack(attackType);
		endField.getUnit().attackWith(attack);

		System.out.println("attackedWith: " + attack.attackDmg + "result h: " + endField.getUnit().getHealth());

		if (!endField.getUnit().isAlive()) {
			controller.getModel().removeUnit(endField.getUnit());

			endField.getUnit().deactivate();
			startField.getUnit().deactivate();

			endField.setUnit(null);
		}

		if (endField.getUnit() == null) {
			var viewQueue = controller.getView().getCommandQueue();
			viewQueue.enqueue(new ClearFieldCommand(endField));
			viewQueue.enqueue(new DrawFieldCommand(endField));
			viewQueue.enqueue(new ClearBattleCommand(controller.getModel().getFields()));
		} else {
			if (iAmAttacking()) {
				attack.attack();
			} else {

				// This is done so that if attacker abort attack, in CtrlAbortAttackCommand 
				// I (as the one defending) can see what was that unit's target. 
				var attacker = startField.getUnit();
				attacker.activateAttack(attack);
				attacker.getActiveAttack().setTarget(endField);

				var dist = controller.getModel().distance(startField, endField);
				var inDefenseRange = dist <= endField.getUnit().getDefense().defenseRange;

				if (!endField.getUnit().isDefending() && inDefenseRange) {
					System.out.println("Strting defense ... ");
					endField.getUnit().getDefense().setTarget(startField);
					endField.getUnit().getDefense().defend();
				} else {
					System.out.println("I am already defending or not in range for defense ... ");
				}
			}
		}

		var descMenuVisible = controller.getView().getDescriptionMenu().isVisible();
		var describingStartField = controller.getFocusedField() == startField;
		var describingEndField = controller.getFocusedField() == endField;

		if (descMenuVisible && (describingStartField || describingEndField)) {
			controller
					.getView()
					.getCommandQueue()
					.enqueue(new ShowFieldDescription(controller.getFocusedField()));
		}

	}

	private boolean iAmAttacking() {
		return !((Controller) targetComponent).isOwner(endField.getPlayer().getUsername());
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
