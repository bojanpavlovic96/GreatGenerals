package model;

import javafx.scene.paint.Color;
import root.model.PlayerData;

public class PlayerModelData  implements PlayerData{

	private String username;

	private Color field_color;

	public PlayerModelData(String username, Color field_color) {
		super();

		this.username = username;
		this.field_color = field_color;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Color getPlayerColor() {
		return field_color;
	}

	public void setPlayerColor(Color field_color) {
		this.field_color = field_color;
	}

}
