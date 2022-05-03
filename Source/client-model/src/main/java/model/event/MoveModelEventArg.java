package model.event;

import root.Point2D;
// import javafx.geometry.Point2D;
import root.model.event.ModelEventArg;

public class MoveModelEventArg extends ModelEventArg {

	public static final String name = "move-model-event";

	// TODO change this to private 
	// switched to public with point2D serialization bug
	public Point2D sourceField; // move from
	public Point2D destinationField; // move to

	// constructors

	public MoveModelEventArg(String player_name, Point2D source_field, Point2D destination_field) {
		super(MoveModelEventArg.name, player_name);

		this.setSourceField(source_field);
		setDestinatoinField(destination_field);

	}

	// getters and setters

	public Point2D getSourceField() {
		return sourceField;
	}

	public void setSourceField(Point2D source_field) {
		this.sourceField = source_field;
	}

	public Point2D getDestinationField() {
		return destinationField;
	}

	public void setDestinatoinField(Point2D destinatoin_field) {
		this.destinationField = destinatoin_field;
	}

}
