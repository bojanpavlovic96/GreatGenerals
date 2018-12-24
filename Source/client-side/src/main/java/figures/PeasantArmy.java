package figures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.Point;

public class PeasantArmy implements Figure {

	private String figure_type;

	private Point position;

	private List<String> available_actions;

	private Map<String, List<ActionHandler>> action_handlers;

	public PeasantArmy(Point position) {

		this.figure_type = "Peasant's army";

		this.position = position;

		this.available_actions = new ArrayList<String>();

		this.action_handlers = new HashMap<String, List<ActionHandler>>();

	}

	public void addActionHandler(String action, ActionHandler action_handler) {

		List<ActionHandler> handlers = this.action_handlers.get(action);

		if (handlers != null) {

			handlers.add(action_handler);

		} else {

			handlers = new ArrayList<ActionHandler>();
			handlers.add(action_handler);

			this.action_handlers.put(action, handlers);
		}
	}

	public List<ActionHandler> getActionHandlers(String action) {
		return this.action_handlers.get(action);
	}

	public ActionHandler getSingleActionHandler(String action, String handler_name) {
		List<ActionHandler> handlers = this.action_handlers.get(action);

		for (ActionHandler handler : handlers) {

			if (((NamedActionHandler) handler).getName().equals(handler_name))
				return handler;
		}

		return null;
	}

	public boolean removeActionHandler(String action, String handler_name) {

		List<ActionHandler> handlers = this.action_handlers.get(action);

		int index = 0;

		for (ActionHandler handler : handlers) {
			if (((NamedActionHandler) handler).getName().equals(handler_name)) {
				handlers.remove(index);
				return true;
			}

			index++;
		}

		return false;
	}

	public String getType() {
		return this.figure_type;
	}

	public Point getPosition() {
		return this.position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

}
