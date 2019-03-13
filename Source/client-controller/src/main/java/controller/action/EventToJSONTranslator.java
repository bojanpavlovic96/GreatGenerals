package controller.action;

import org.json.JSONObject;
import model.event.ModelEventArg;

public interface EventToJSONTranslator {
	
	JSONObject translate(ModelEventArg model_event);
	
}
