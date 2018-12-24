package board;

import figures.Figure;

public interface Field {

	Point getPosition();

	boolean containsFigure();

	Figure getFigure();

	/**
	 * @return this, field from which figure is removed. it enables chaining of the
	 *         function calls on the same field;
	 */
	Field removeFigure();

	/**
	 * @param new_figure
	 * @return this, field on which figure is going to be placed. it enables
	 *         chaining of the function calls on the same field.
	 */
	Field setFigure(Figure new_figure);

	void setFieldArgs(FieldArgs ars);

	FieldArgs getFieldArgs();

}
