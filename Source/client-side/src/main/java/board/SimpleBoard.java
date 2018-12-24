package board;

import actions.Move;
import ui.Printable;

public class SimpleBoard implements Board, Printable {

	private Field[][] fields;

	private int fields_dim;
	private int fields_num;

	public SimpleBoard(int fields_dim) {

		this.fields_dim = fields_dim;
		this.fields_num = this.fields_dim * this.fields_dim;

		this.initializeBoard();

	}

	public SimpleBoard(Field[][] fields, int fields_num) {
		
		this.fields = fields;

		this.fields_dim = this.fields.length;
		this.fields_num = fields_num;

	}

	private void initializeBoard() {
		this.fields = new Field[this.fields_num][this.fields_num];

		for (int i = 0; i < this.fields_num; i++) {
			for (int j = 0; j < this.fields_num; j++) {

				// create empty field

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

}
