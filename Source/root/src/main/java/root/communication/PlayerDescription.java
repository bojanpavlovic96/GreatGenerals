package root.communication;

import root.model.PlayerData;
import root.view.Color;

public class PlayerDescription implements PlayerData {

	private String username;

	private Color color;

	public PlayerDescription() {
	}

	public PlayerDescription(String username, Color color) {
		this.username = username;
		this.color = color;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	// setters could be required for serialization
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	// setters could be required for serialization
	public void setColor(Color color) {
		this.color = color;
	}
}
