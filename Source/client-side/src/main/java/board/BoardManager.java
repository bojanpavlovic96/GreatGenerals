package board;

import actions.Move;

public interface BoardManager {

	Board getBoard();

	void setBoard(Board board);

	FigureCreator getFigureCreator();

	void setFigureCreator(FigureCreator creator);

	void setBoardLoader(BoardLoader board_loader);

	BoardLoader getBoardLoader();

}
