package model;

import root.model.PlayerData;
import root.view.Color;

public class PlayerModelData implements PlayerData {

	private String username;

	private Color fieldColor;

	public PlayerModelData(String username, Color field_color) {
		super();

		this.username = username;
		this.fieldColor = field_color;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public Color getColor() {
		return this.fieldColor;
	}

}
