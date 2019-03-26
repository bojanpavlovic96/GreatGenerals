package model.event;

import javafx.geometry.Point2D;
import root.model.event.ModelEventArg;

public class MoveModelEventArg extends ModelEventArg {

	private Point2D source_field; // move from
	private Point2D destinatoin_field; // move to

	// constructors

	public MoveModelEventArg(String player_name, Point2D source_field, Point2D destination_field) {
		super("move-model-event", player_name);

		this.setSourceField(source_field);
		setDestinatoinField(destination_field);

	}

	// getters and setters

	public Point2D getSourceField() {
		return source_field;
	}

	public void setSourceField(Point2D source_field) {
		this.source_field = source_field;
	}

	public Point2D getDestinationField() {
		return destinatoin_field;
	}

	public void setDestinatoinField(Point2D destinatoin_field) {
		this.destinatoin_field = destinatoin_field;
	}

}
