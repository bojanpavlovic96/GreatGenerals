package model.event;

import root.Point2D;
import root.model.event.ModelEventArg;

public class DefendModelEventArg extends ModelEventArg {

	public String defenseType;

	public Point2D sourceField;
	public Point2D destinationField;

	public DefendModelEventArg(String defenseType, String playerName, Point2D srcField, Point2D destField) {
		super(ModelEventType.Defend, playerName);

		this.defenseType = defenseType;
		this.sourceField = srcField;
		this.destinationField = destField;
	}

}
