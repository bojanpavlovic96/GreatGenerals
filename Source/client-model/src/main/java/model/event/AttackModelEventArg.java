package model.event;

import root.Point2D;
import root.model.event.ModelEventArg;

public class AttackModelEventArg extends ModelEventArg {

	public Point2D sourceField;
	public Point2D destinationField;

	public AttackModelEventArg(String playerName, Point2D srcField, Point2D destField) {
		super(ModelEventArg.ModelEventType.Attack, playerName);

		this.sourceField = srcField;
		this.destinationField = destField;
	}

	public Point2D getSourceField() {
		return sourceField;
	}

	public void setSourceField(Point2D source_field) {
		this.sourceField = source_field;
	}

	public Point2D getDestinationField() {
		return destinationField;
	}

	public void setDestinationField(Point2D destination_field) {
		this.destinationField = destination_field;
	}

}
