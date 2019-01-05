package view;

import javafx.event.Event;
import javafx.geometry.Point2D;

public class ViewEvent {

	private Event event;

	private Point2D field_position;

	public ViewEvent(Event event, Point2D field_position) {
		super();
		this.event = event;
		this.field_position = field_position;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Point2D getField_position() {
		return field_position;
	}

	public void setField_position(Point2D field_position) {
		this.field_position = field_position;
	}

}
