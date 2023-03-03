package model.intention;

import root.Point2D;
import root.model.event.ClientIntention;

public class AbortAttackIntention extends ClientIntention {

	public Point2D position;

	public AbortAttackIntention(Point2D position, String playerName) {
		super(ClientIntentionType.AbortAttack, playerName);

		this.position = position;
	}

}
