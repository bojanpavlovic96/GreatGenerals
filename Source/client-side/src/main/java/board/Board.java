package board;

import actions.Move;

public interface Board {

	void makeMove(Move move);
	
	void printCurrentTable();
	
}
