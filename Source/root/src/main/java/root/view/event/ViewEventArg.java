package root.view.event;

import javafx.event.Event;
import javafx.geometry.Point2D;

public class ViewEventArg {

	private Event event;

	private Point2D position;

	public ViewEventArg(Event event, Point2D positoin) {
		this.event = event;
		this.position = positoin;

	}

	public Event getEvent() {
		return this.event;
	}

	public Point2D getFieldPosition() {
		return this.position;
	}

}
