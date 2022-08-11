package app.form;

public class PlayerListItem {

	private String name;
	private int level;

	public PlayerListItem(String name, int level) {
		this.name = name;
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}
}
