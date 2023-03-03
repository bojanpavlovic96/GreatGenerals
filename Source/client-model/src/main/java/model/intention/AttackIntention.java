package model.intention;

import root.Point2D;
import root.model.event.ClientIntention;

public class AttackIntention extends ClientIntention {

	public String attackType;

	public Point2D sourceField;
	public Point2D destinationField;

	public AttackIntention(String attackType, String playerName, Point2D srcField, Point2D destField) {
		super(ClientIntentionType.Attack, playerName);

		this.attackType = attackType;
		this.sourceField = srcField;
		this.destinationField = destField;
	}

	public Point2D getSourceField() {
		return sourceField;
	}

	public void setSourceField(Point2D sourceField) {
		this.sourceField = sourceField;
	}

	public Point2D getDestinationField() {
		return destinationField;
	}

	public void setDestinationField(Point2D destinationField) {
		this.destinationField = destinationField;
	}

}
