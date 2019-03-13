package controller.communication;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import controller.action.EventToJSONTranslator;
import controller.communication.ServerMessage;
import controller.communication.ServerMessageTranslator;
import model.event.ModelEventArg;
import model.event.MoveModelEventArg;

public class JSONMessageTranslator extends ServerMessageTranslator {

	private Map<String, EventToJSONTranslator> event_to_json_translators;

	// constructors

	public JSONMessageTranslator() {
		super();

		this.initializeTranslatorMap();

	}

	// methods

	private void initializeTranslatorMap() {

		// map all events to appropriate event translators

		this.event_to_json_translators = new HashMap<String, EventToJSONTranslator>();

		this.event_to_json_translators.put("move-model-event", new EventToJSONTranslator() {

			public JSONObject translate(ModelEventArg model_event) {

				MoveModelEventArg move_event = (MoveModelEventArg) model_event;

				// get source
				// get destination
				// pack to json
				// add player username
				// ...
				// return json

				return null;
			}
		});

		this.event_to_json_translators.put("attack-model-event", new EventToJSONTranslator() {

			public JSONObject translate(ModelEventArg model_event) {

				return null;
			}
		});

	}

	// public methods

	// attention this method should return ctrlCommand
	@Override
	public ServerMessage translate(byte[] source) {

		return null;
	}

	@Override
	public byte[] translate(ModelEventArg model_event) {

		JSONObject json = this.event_to_json_translators.get(model_event.getName()).translate(model_event);

		// maybe add something more to generated object
		// probably not

		return json.toString().getBytes();
	}

}
