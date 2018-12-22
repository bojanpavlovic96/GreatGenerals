package actions;

import board.Point;
import figures.Figure;

public class Move {

	private Point prev_position;

	private Point next_position;

	private Figure figure;
	
	public Move(Point prev_point, Point next_point) {

		prev_position = prev_point;
		next_position = next_point;

	}

	public Point getPrev_position() {
		return prev_position;
	}

	public void setPrev_position(Point prev_position) {
		this.prev_position = prev_position;
	}

	public Point getNext_position() {
		return next_position;
	}

	public void setNext_position(Point next_position) {
		this.next_position = next_position;
	}

	public Figure getFigure() {
		return figure;
	}

	public void setFigure(Figure figure) {
		this.figure = figure;
	}

}
