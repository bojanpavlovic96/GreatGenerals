package board;

import actions.Move;

public interface BoardManager {

	void initializeBoard();

	BoardManager makeMove(Move move); // if valid make move and return boardManager

	boolean isValidMove(Move move);

}
