package model.event;

import javafx.geometry.Point2D;
import root.model.event.ModelEventArg;

public class AttackModelEventArg extends ModelEventArg {

	public static final String name = "attack-model-event";

	// TODO change this to private 
	// switched to public with point2D serialization bug
	public Point2D sourceField;
	public Point2D destinationField;

	// constructors

	public AttackModelEventArg(String player_name, Point2D source_field, Point2D destination_field) {
		super(AttackModelEventArg.name, player_name);

		this.sourceField = source_field;
		this.destinationField = destination_field;

	}

	// getters and setters

	public Point2D getSource_field() {
		return sourceField;
	}

	public void setSource_field(Point2D source_field) {
		this.sourceField = source_field;
	}

	public Point2D getDestination_field() {
		return destinationField;
	}

	public void setDestination_field(Point2D destination_field) {
		this.destinationField = destination_field;
	}

}
