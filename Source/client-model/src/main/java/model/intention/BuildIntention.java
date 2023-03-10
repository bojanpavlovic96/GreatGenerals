package model.intention;

import root.Point2D;
import root.model.event.ClientIntention;

public class BuildIntention extends ClientIntention {

	private Point2D field;

	private String unitType;

	private int cost;

	public BuildIntention(String playerName, Point2D field, String unitType, int cost) {
		super(ClientIntentionType.BuildUnit, playerName);

		this.field = field;
		this.unitType = unitType;
		this.cost = cost;
	}

	public Point2D getField() {
		return field;
	}

	public void setField(Point2D field) {
		this.field = field;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
}
