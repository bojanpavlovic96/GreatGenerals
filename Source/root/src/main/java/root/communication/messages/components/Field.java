package root.communication.messages.components;

import root.Point2D;

public class Field {

	public Point2D positions;

	public boolean isVisible;
	public Unit unit;
	public Terrain terrain;
	public PlayerData playerData; // TODO is this necessary ... ? 

	public boolean inBattle;

}
