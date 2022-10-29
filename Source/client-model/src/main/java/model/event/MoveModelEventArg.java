package model.event;

import root.Point2D;
import root.model.event.ModelEventArg;

public class MoveModelEventArg extends ModelEventArg {

	private Point2D sourceField;
	private Point2D destinationField;

	public MoveModelEventArg(String playerName,
			Point2D sourceField,
			Point2D destinationField) {
		super(ModelEventArg.ModelEventType.Move, playerName);

		setSourceField(sourceField);
		setDestinationField(destinationField);
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
