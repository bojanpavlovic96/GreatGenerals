package root.view.event;

import root.Point2D;

public class ViewEventArg {

	private Point2D position;

	private String key;

	public ViewEventArg(Point2D position) {
		this.position = position;
		this.key = null;
	}

	public ViewEventArg(String key) {
		this.position = null;
		this.key = key;
	}

	public Point2D getFieldPosition() {
		return this.position;
	}

	public String getKey() {
		return this.key;
	}

}
