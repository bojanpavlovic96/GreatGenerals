package board;

import actions.Move;

public interface Board {

	void makeMove(Move move);

	Field getField(int x, int y);

	void setField(Field field, int x, int y);

	int getFieldsDim();

	int getFieldsNum();

}
