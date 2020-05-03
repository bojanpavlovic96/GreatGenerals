package root.command;

public abstract class Command implements Runnable {

	protected String name;

	protected CommandDrivenComponent targetComponent;

	// constructors

	public Command(String name) {
		this.name = name;
	}

	// methods

	public void setTargetComponent(CommandDrivenComponent target) {
		this.targetComponent = target;
	}

	public CommandDrivenComponent getTargetComponent() {
		return this.targetComponent;
	}

	public String getName() {
		return this.name;
	}

	public abstract Command getAntiCommand();

}
