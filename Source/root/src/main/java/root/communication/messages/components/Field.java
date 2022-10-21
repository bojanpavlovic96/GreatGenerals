package root.communication.messages.components;

import root.Point2D;
import root.model.PlayerData;

public class Field {

	public Point2D positions;

	public boolean isVisible;
	public Unit unit;
	public Terrain terrain;

	public boolean inBattle;

	public PlayerData owner;

}
