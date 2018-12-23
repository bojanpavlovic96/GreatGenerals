package board;

import figures.Figure;

public interface FigureCreator {

	Figure createFigure(String figure_name);

	Figure getPrototype(String prototype_name);

	void addPrototype(String prototype_name, Figure prototype);

}
