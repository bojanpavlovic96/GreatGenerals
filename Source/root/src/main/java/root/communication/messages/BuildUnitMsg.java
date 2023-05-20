package root.communication.messages;

import java.util.Date;

import root.Point2D;

public class BuildUnitMsg extends Message {

	public Point2D field;
	public String unitType;
	public int cost;

	public BuildUnitMsg(Date timestamp, Point2D field, String unitType, int cost) {
		super(MessageType.BuildUnit, timestamp);

		this.field = field;
		this.unitType = unitType;
		this.cost = cost;
	}

}
