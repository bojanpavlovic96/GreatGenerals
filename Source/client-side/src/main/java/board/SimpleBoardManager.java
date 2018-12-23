package board;

import actions.Move;

public class SimpleBoardManager implements BoardManager {

	private Board board;
	
	public SimpleBoardManager() {

	}
	
	public SimpleBoardManager(Board board) {
		this.board = board;
		
	}

	public boolean isValidMove(Move move) {
		// TODO Auto-generated method stub
		return false;
	}

	public BoardManager makeMove(Move move) {
		// TODO Auto-generated method stub
		return null;
	}

	public Board getBoard() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBoard(Board board) {
		// TODO Auto-generated method stub
		
	}

}
