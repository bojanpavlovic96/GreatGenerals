package figures;

public abstract class NamedActionHandler implements ActionHandler {

	private String name;

	public NamedActionHandler(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
