package communication;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;

import controller.command.CtrlInitializeCommand;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.PlayerModelData;
import model.component.field.ModelField;
import root.command.Command;
import root.command.CommandQueue;
import root.communication.MessageTranslator;
import root.communication.ServerProxy;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.Terrain;
import root.model.event.ModelEventArg;

public class BasicServerProxy implements ServerProxy {

	private Channel channel;

	private MessageTranslator translator;

	private CommandQueue command_queue;

	// used for calculating routing key
	private String username;
	private String room_name;

	// constructors

	public BasicServerProxy(Channel channel, MessageTranslator translator, String username,
			String room_name) {
		this.channel = channel;
		this.translator = translator;

		this.room_name = room_name;
		this.room_name = room_name;

		this.command_queue = new CommandQueue();

		this.initCommunicationChannel();

	}

	// methods

	// implement create queues and stuff
	private void initCommunicationChannel() {
		// configure channels and start listening for server messages

		// first message must be model initialization message

		// attention do not start receiving messages until queue is set !!!

		// fake first initialization message
		List<PlayerData> players = new ArrayList<PlayerData>();
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
					field_models.add(new ModelField(new Point2D(j, i), players.get(player_counter), true,
							null, new Terrain("mountains", 1)));
				else
					field_models.add(new ModelField(new Point2D(j, i), players.get(player_counter), true,
							null, new Terrain("water", 1)));

				player_counter++;
				player_counter %= 3;

			}

			if (left > -3)
				left--;
		}

		this.command_queue.enqueue(new CtrlInitializeCommand(players, field_models));

	}

	@Override
	public Channel getCommunicationChannel() {
		return this.channel;
	}

	@Override
	public void setCommunicationChannel(Channel new_channel) {
		this.channel = new_channel;

	}

	@Override
	public MessageTranslator getMessageTranslator() {
		return this.translator;
	}

	@Override
	public void setMessageTranslator(MessageTranslator new_translator) {
		this.translator = new_translator;
	}

	@Override
	public void setConsumerQueue(CommandQueue consumer_queue) {
		this.command_queue = consumer_queue;
	}

	@Override
	public CommandQueue getConsumerQueue() {
		return this.command_queue;
	}

	@Override
	public void sendIntention(ModelEventArg action) {

		// debug
		System.out.println("Sending intention: " + action.getEventName() + "@ BasicServeProxy.sendIntention");

		byte[] message = this.translator.translate(action);

		Command received_command = this.translator.translate(message);

		this.command_queue.enqueue(received_command);

		// TODO somehow send it through channel

	}

}
