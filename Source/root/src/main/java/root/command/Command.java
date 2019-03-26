package root.command;

public abstract class Command implements Runnable {

	protected String name;

	protected CommandDrivenComponent target_component;

	// constructors

	public Command(String name) {
		this.name = name;
	}

	// methods

	public void setTargetComponent(CommandDrivenComponent target) {
		this.target_component = target;
	}

	public CommandDrivenComponent getTargetComponent() {
		return this.target_component;
	}

	public String getName() {
		return this.name;
	}

	public abstract Command getAntiCommand();

}
