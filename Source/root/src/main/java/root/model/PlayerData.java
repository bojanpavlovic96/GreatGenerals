package root.model;

import root.view.Color;

public interface PlayerData {

	String getUsername();

	Color getColor();

	int getLevel();

	int getCoins();

	void setCoins(int amount);

	void removeCoins(int amount);

	void setPoints(int totalAmount);

	int getPoints();
}
