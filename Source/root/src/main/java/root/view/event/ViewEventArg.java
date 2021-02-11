package root.view.event;

import javafx.geometry.Point2D;

public class ViewEventArg {

	private Point2D position;

	public ViewEventArg(Point2D positoin) {
		this.position = positoin;

	}

	public Point2D getFieldPosition() {
		return this.position;
	}

}
