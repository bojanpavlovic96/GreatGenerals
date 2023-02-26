package root.command;

// Maybe. 
// Split this interface in just command and undoable command.
// None of the ctrlCommands are gonna be undone, so no reason for them to have
// getAntiCommnad method. 
public abstract class Command implements Runnable {

	protected CommandDrivenComponent targetComponent;

	public void setTargetComponent(CommandDrivenComponent target) {
		this.targetComponent = target;
	}

	public CommandDrivenComponent getTargetComponent() {
		return this.targetComponent;
	}

	public abstract Command getAntiCommand();

}
