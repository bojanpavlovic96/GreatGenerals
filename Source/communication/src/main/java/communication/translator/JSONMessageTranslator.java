package communication.translator;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import controller.command.CtrlMoveCommand;
import javafx.geometry.Point2D;
import model.event.MoveModelEventArg;
import root.command.Command;
import root.communication.MessageTranslator;
import root.model.event.ModelEventArg;

public class JSONMessageTranslator implements MessageTranslator {

	private Map<String, MessageTranslator> message_translators;

	// constructors

	public JSONMessageTranslator() {
		super();

		this.initializeTranslatorMap();
	}

	// methods

	private void initializeTranslatorMap() {

		// map all events to appropriate event translators

		this.message_translators = new HashMap<String, MessageTranslator>();

		this.message_translators.put("move-model-event", new MessageTranslator() {

			@Override
			public byte[] translate(ModelEventArg model_action) {

				MoveModelEventArg move_event = (MoveModelEventArg) model_action;

				JSONObject json = new JSONObject();

				json.put("event_name", move_event.getEventName());
				json.put("player_name", move_event.getPlayerName());

				json.put("source_field_x", move_event.getSourceField().getX());
				json.put("source_field_y", move_event.getSourceField().getY());
				json.put("destination_field_x", move_event.getDestinationField().getX());
				json.put("destination_field_y", move_event.getDestinationField().getY());

				return json.toString().getBytes();
			}

			@Override
			public Command translate(byte[] source) {

				JSONObject json = new JSONObject(new String(source));

				Point2D first_postition = new Point2D(	json.getDouble("source_field_x"),
														json.getDouble("source_field_y"));

				Point2D second_position = new Point2D(	json.getDouble("destination_field_x"),
														json.getDouble("destination_field_y"));

				return new CtrlMoveCommand(first_postition, second_position);

			}
			
		});

	}

	// public methods

	// attention this method should return ctrlCommand
	@Override
	public Command translate(byte[] source) {

		JSONObject json = new JSONObject(new String(source));

		return this.message_translators.get(json.get("event_name")).translate(source);
	}

	@Override
	public byte[] translate(ModelEventArg model_event) {

		return this.message_translators.get(model_event.getEventName()).translate(model_event);

	}

}
