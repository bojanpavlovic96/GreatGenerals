package root.communication.messages.components;

import root.view.Color;

public class PlayerData {

	public String username;
	public Color color;

	public PlayerData() {

	}

	public PlayerData(String username, Color color) {
		this.username = username;
		this.color = color;
	}

}
