package root.communication.messages.components;

import root.Point2D;

public class FieldDesc {

	public Point2D position;

	public boolean isVisible;

	// attention enum on the server side 
	public String unit;
	
	public TerrainDesc terrain;

	public boolean inBattle;

	public String owner;

	public FieldDesc() {

	}

}
