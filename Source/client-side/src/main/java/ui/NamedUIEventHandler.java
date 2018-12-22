package ui;

public class NamedUIEventHandler implements UIEventHandler {

	private String name;

	public NamedUIEventHandler(String name) {

		this.name = name;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
