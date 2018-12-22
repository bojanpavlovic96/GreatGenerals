package board;

import actions.Move;

public class SimpleBoard implements Board {

	private Field[][] fields;

	private int fields_num;

	public SimpleBoard() {

		this.fields_num = 20;

		this.initializeBoard();

	}

	private void initializeBoard() {
		this.fields = new Field[this.fields_num][this.fields_num];

		for (int i = 0; i < this.fields_num; i++) {
			for (int j = 0; j < this.fields_num; j++) {
				 this.fields[i][j] = (Field) new EmptyField(new Point(i, j)); // create empty field
			}
		}
	}

	private Field getField(Point position) {
		return this.fields[position.getX()][position.getY()];
	}
	
	public void makeMove(Move move) {
		
		this.getField(move.getPrev_position()).removeFigure();
		this.getField(move.getNext_position()).setFigure(move.getFigure());
		
	}

	public void printCurrentTable() {
		for (int i = 0; i < this.fields_num; i++) {
			for (int j = 0; j < this.fields_num; j++) {
				this.fields[i][j].printField();
			}
		}

	}

}
