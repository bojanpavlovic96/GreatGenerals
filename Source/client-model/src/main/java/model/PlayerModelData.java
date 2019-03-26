package model;

import javafx.scene.paint.Color;
import root.model.PlayerData;

public class PlayerModelData implements PlayerData {

	private String username;

	private Color field_color;

	public PlayerModelData(String username, Color field_color) {
		super();

		this.username = username;
		this.field_color = field_color;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public Color getColor() {
		return this.field_color;
	}

}
