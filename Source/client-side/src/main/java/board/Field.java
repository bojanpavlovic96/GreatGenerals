package board;

import figures.Figure;

public interface Field {

	void printField();

	Point getPosition();

	Figure getFigure();

	/**
	 * @return this, field from which figure is removed. it enables chaining of the
	 *         function calls on the same field;
	 */
	Field removeFigure();

	/**
	 * @param new_figure
	 * @return this, field on which figure is going to be places. it enables
	 *         chaining of the function calls on the same field.
	 */
	Field setFigure(Figure new_figure);
	
}
