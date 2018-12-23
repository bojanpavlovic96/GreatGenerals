package board;

import actions.Move;

public interface BoardManager {

	Board getBoard();

	void setBoard(Board board);

	FigureCreator getFigureCreator();

	void setFigureCreator(FigureCreator creator);

}
