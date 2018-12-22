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
				this.fields[i][j] = new Field(new Point(i,j)); // create empty field
			}
		}
	}

	public void makeMove(Move move) {
		// TODO Auto-generated method stub
		
	}

	public void printCurrentTable() {
		for (int i = 0; i < this.fields_num; i++) {
			for (int j = 0; j < this.fields_num; j++) {
				this.fields[i][j]
			}
		}
		
	}

}
