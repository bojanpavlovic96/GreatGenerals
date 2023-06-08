package root.communication;

import root.model.PlayerData;
import root.view.Color;

public class PlayerDescription implements PlayerData {

	private String username;

	private Color color;

	private int level;

	private int coins;

	private int points;

	public PlayerDescription() {
	}

	public PlayerDescription(String username, int level, int points) {
		this.username = username;
		this.level = level;
		this.points = points;
	}

	public PlayerDescription(String username, Color color, int level, int points) {
		this(username, level, points);
		this.color = color;
		// this.username = username;
		// this.level = level;
		// this.points = points;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public int getCoins() {
		return this.coins;
	}

	// required for serialization (some of them) ... 

	public void setLevel(int level) {
		this.level = level;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void setCoins(int amount) {
		this.coins = amount;
	}

	@Override
	public void removeCoins(int amount) {
		coins -= amount;
		if (coins < 0) {
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