package model;

import root.model.PlayerData;
import root.view.Color;

public class PlayerModelData implements PlayerData {

	private String username;

	private Color fieldColor;

	private int level;
	private int coins;

	private int points;

	public PlayerModelData(String username, Color fieldColor, int level, int points) {
		this.username = username;
		this.fieldColor = fieldColor;
		this.level = level;
		this.points = points;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public Color getColor() {
		return this.fieldColor;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public int getCoins() {
		return this.coins;
	}

	@Override
	public void setCoins(int amount) {
		this.coins = amount;
	}

	@Override
	public void removeCoins(int amount) {
		this.coins -= amount;
		if (this.coins < 0) {
			coins = 0;
		}
	}

	@Override
	public void setPoints(int totalAmount) {
		this.points = totalAmount;
	}

	@Override
	public int getPoints() {
		return this.points;
	}

}
