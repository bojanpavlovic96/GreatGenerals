package model.component.unit;

import model.component.field.Field;

public interface MoveEventHandler {

	void execute(Field from, Field to);

}
