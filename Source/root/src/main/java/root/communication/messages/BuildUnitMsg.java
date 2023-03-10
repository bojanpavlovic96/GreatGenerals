package root.communication.messages;

import root.Point2D;

public class BuildUnitMsg extends Message {

	public Point2D field;
	public String unitType;
	public int cost;

	public BuildUnitMsg(Point2D field, String unitType, int cost) {
		super(MessageType.BuildUnit);

		this.field = field;
		this.unitType = unitType;
		this.cost = cost;
	}

}
