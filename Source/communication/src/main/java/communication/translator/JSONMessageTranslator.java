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

	private Map<String, MessageTranslator> messageTranslator;

	// constructors

	// TODO this class should be implemented as simple jsonSerialization
	//
	// yes ... com.google.guava or jackson should replace this class
	public JSONMessageTranslator() {
		super();

		this.initializeTranslatorMap();
	}

	// methods

	private void initializeTranslatorMap() {

		// map all events to appropriate event translators

		this.messageTranslator = new HashMap<String, MessageTranslator>();

		this.messageTranslator.put("move-model-event", new MessageTranslator() {

			@Override
			public byte[] toByte(ModelEventArg modelAction) {

				MoveModelEventArg moveEvent = (MoveModelEventArg) modelAction;

				JSONObject json = new JSONObject();

				json.put("event_name", moveEvent.getEventName());
				json.put("player_name", moveEvent.getPlayerName());

				json.put("source_field_x", moveEvent.getSourceField().getX());
				json.put("source_field_y", moveEvent.getSourceField().getY());
				json.put("destination_field_x", moveEvent.getDestinationField().getX());
				json.put("destination_field_y", moveEvent.getDestinationField().getY());

				return json.toString().getBytes();
			}

			@Override
			public Command toCommand(byte[] source) {

				JSONObject json = new JSONObject(new String(source));

				Point2D firstPosition = new Point2D(json.getDouble("source_field_x"),
						json.getDouble("source_field_y"));

				Point2D secondPosition = new Point2D(json.getDouble("destination_field_x"),
						json.getDouble("destination_field_y"));

				return new CtrlMoveCommand(firstPosition, secondPosition);

			}

		});

	}

	// public methods

	// attention this method should return ctrlCommand
	@Override
	public Command toCommand(byte[] source) {

		JSONObject json = new JSONObject(new String(source));

		return this.messageTranslator.get(json.get("event_name")).toCommand(source);
	}

	@Override
	public byte[] toByte(ModelEventArg modelEvent) {

		return this.messageTranslator.get(modelEvent.getEventName()).toByte(modelEvent);

	}

}
