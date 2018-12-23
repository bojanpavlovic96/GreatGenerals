package board;

import actions.Move;

public interface BoardManager {

	boolean isValidMove(Move move);

	BoardManager makeMove(Move move); // if valid make move and return boardManager

	Board getBoard();

	void setBoard(Board board);

}
