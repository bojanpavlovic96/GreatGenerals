package view;

import java.awt.geom.Point2D;

import board.Field;

public interface FieldPainter {

	void updateField(Field field);

	void showStatus(Field field);

	Point2D translateCoordinatesToPosition(Point2D coordinates); // converts coordinates from (e.g.) mouse event to
																	// position from board representation

	Point2D translatePositionToCoordinates(Point2D position); // converts position from board representation to real
																// coordinates where it is going to be painted

}
