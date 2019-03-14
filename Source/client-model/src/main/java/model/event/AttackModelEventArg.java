package model.event;

import javafx.geometry.Point2D;

public class AttackModelEventArg extends ModelEventArg {

	private Point2D source_field;
	private Point2D destination_field;

	// constructors

	public AttackModelEventArg(String player_name, Point2D source_field, Point2D destination_field) {
		super("attack-model-event", player_name);

		this.source_field = source_field;
		this.destination_field = destination_field;

	}

	// getters and setters

	public Point2D getSource_field() {
		return source_field;
	}

	public void setSource_field(Point2D source_field) {
		this.source_field = source_field;
	}

	public Point2D getDestination_field() {
		return destination_field;
	}

	public void setDestination_field(Point2D destination_field) {
		this.destination_field = destination_field;
	}

}
