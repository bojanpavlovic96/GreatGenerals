package model;

import javafx.scene.paint.Color;

public class PlayerData {

	private String username;
	private Color field_color;

	public PlayerData(String username, Color field_color) {
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

	public Color getField_color() {
		return field_color;
	}

	public void setField_color(Color field_color) {
		this.field_color = field_color;
	}

}
