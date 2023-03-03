package model.intention;

import root.Point2D;
import root.model.event.ClientIntention;

public class DefendIntention extends ClientIntention {

	public String defenseType;

	public Point2D sourceField;
	public Point2D destinationField;

	public DefendIntention(String defenseType, String playerName, Point2D srcField, Point2D destField) {
		super(ClientIntentionType.Defend, playerName);

		this.defenseType = defenseType;
		this.sourceField = srcField;
		this.destinationField = destField;
	}

}
