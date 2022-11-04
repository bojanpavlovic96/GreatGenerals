package root.command;

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
