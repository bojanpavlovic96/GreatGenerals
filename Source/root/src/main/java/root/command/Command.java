package root.command;

public abstract class Command implements Runnable {

	// protected CommandType type;

	protected CommandDrivenComponent targetComponent;

	public Command() {
		// this.type = type;
	}

	public void setTargetComponent(CommandDrivenComponent target) {
		this.targetComponent = target;
	}

	public CommandDrivenComponent getTargetComponent() {
		return this.targetComponent;
	}

	// public CommandType getType() {
	// 	return this.type;
	// }

	public abstract Command getAntiCommand();

}
