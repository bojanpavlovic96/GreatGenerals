package figures;

import java.util.List;

import actions.Move;
import board.Field;
import board.Point;
import rabbit.api.Messenger;

public interface Figure extends Cloneable {

	String getType();
	
	Point getPosition();

	void setPosition(Point position);
	
	void addActionHandler(String action, ActionHandler action_handler);

	List<ActionHandler> getActionHandlers(String action);

	ActionHandler getSingleActionHandler(String action, String handler_name);

	boolean removeActionHandler(String action, String handler);
	
}
