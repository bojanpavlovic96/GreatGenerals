package controller.communication;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;

import controller.command.CtrlCommandQueue;
import controller.command.CtrlInitializeCommand;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.PlayerModelData;
import model.component.field.GameField;
import root.command.CommandQueue;
import root.model.component.Field;
import root.model.component.Terrain;
import root.model.event.ModelEventArg;
import server.Server;

public class ServerProxy implements Server, Communicator {

	private Channel channel;

	private ServerMessageTranslator message_translator;

	private CommandQueue ctrl_queue;

	// constructors

	public ServerProxy(Channel channel, ServerMessageTranslator translator) {

		this.channel = channel;
		this.message_translator = translator;

		this.initCommunicationChannel();

	}

	// methods
	// implement
	private void initCommunicationChannel() {

		// configure channels and start listening for server messages

		// first message must be model initialization message

		// attention do not start receiving messages until queue is set !!!

	}

	// server methods

	public void sendIntention(ModelEventArg model_event) {

		// debug
		System.out.println("Sending intention ... " + model_event.getEventName()
							+ " @ ServerProxy.sendIntention");

		byte[] message_to_send = this.message_translator.translate(model_event);

		this.ctrl_queue.enqueue(this.message_translator.translate(message_to_send));

	}

	// communicator methods

	public Channel getCommunicationChannel() {
		return this.channel;
	}

	public CommandQueue getCtrlQueue() {
		return this.ctrl_queue;
	}

	// getters and setters

	public void setCtrlQueue(CommandQueue queue) {
		this.ctrl_queue = queue;

		// attention start receiving messages from server

		// let say this is some content from the server

		List<PlayerModelData> players = new ArrayList<PlayerModelData>();
		players.add(new PlayerModelData("user 1", Color.RED));
		players.add(new PlayerModelData("user 2", Color.GREEN));
		players.add(new PlayerModelData("user 3", Color.BLACK));

		List<Field> field_models = new ArrayList<Field>();

		int left = 3;
		int right = 17;

		int player_counter = 0;

		for (int i = 1; i < 16; i++) {

			for (int j = left; j < right; j++) {
				if (i % 2 == 0 && j % 5 == 0)
					field_models.add(new GameField(	new Point2D(j, i),
													players.get(player_counter),
													true,
													null,
													new Terrain("mountains", 1)));
				else
					field_models.add(new GameField(	new Point2D(j, i),
													players.get(player_counter),
													true,
													null,
													new Terrain("water", 1)));

				player_counter++;
				player_counter %= 3;

			}

			if (left > -3)
				left--;
		}

		this.ctrl_queue.enqueue(new CtrlInitializeCommand(null, players, field_models));

	}

	public ServerMessageTranslator getMessageTranslator() {
		return message_translator;
	}

}
