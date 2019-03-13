package controller.action;

import org.json.JSONObject;
import model.event.ModelEvent;

public interface EventToJSONTranslator {
	
	JSONObject translate(ModelEvent model_event);
	
}
