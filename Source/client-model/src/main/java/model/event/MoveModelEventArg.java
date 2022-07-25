package model.event;

import root.Point2D;
// import javafx.geometry.Point2D;
import root.model.event.ModelEventArg;

public class MoveModelEventArg extends ModelEventArg {

	// TODO change this to private 
	// switched to public with point2D serialization bug
	public Point2D sourceField; // move from
	public Point2D destinationField; // move to

	public MoveModelEventArg(String player_name, Point2D sourceField, Point2D destinationField) {
		super(ModelEventArg.EventType.MoveModelEvent, player_name);

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
