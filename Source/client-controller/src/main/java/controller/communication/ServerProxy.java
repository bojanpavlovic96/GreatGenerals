package controller.communication;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;

import controller.command.CtrlCommandQueue;
import controller.command.CtrlInitializeCommand;
import controller.command.CtrlMoveCommand;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.PlayerData;
import model.component.Terrain;
import model.component.field.Field;
import model.component.field.GameField;
import model.component.unit.MoveEventHandler;
import server.Server;

public class ServerProxy implements Server, Communicator {

	private Channel channel;

	private ServerMessageTranslator message_translator;

	private CtrlCommandQueue ctrl_queue;

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

	// TODO add argument ... what to send
	public void sendIntention() {

	}

	// communicator methods

	public Channel getCommunicationChannel() {
		return this.channel;
	}

	public CtrlCommandQueue getCtrlQueue() {
		return this.ctrl_queue;
	}

	public void setCtrlQueue(CtrlCommandQueue queue) {
		this.ctrl_queue = queue;

		// attention start receiving messages from server

		// let say this is some content from the server

		List<PlayerData> players = new ArrayList<PlayerData>();
		players.add(new PlayerData("user 1", Color.RED));
		players.add(new PlayerData("user 2", Color.GREEN));
		players.add(new PlayerData("user 3", Color.WHITE));

		List<Field> field_models = new ArrayList<Field>();

		int left = 3;
		int right = 17;

		int player_counter = 0;

		for (int i = 1; i < 16; i++) {

			for (int j = left; j < right; j++) {
				if (i % 2 == 0 && j % 5 == 0)
					field_models.add(new GameField(new Point2D(j, i), players.get(player_counter), true, null,
							new Terrain("mountains", 1)));
				else
					field_models.add(new GameField(new Point2D(j, i), players.get(player_counter), true, null,
							new Terrain("water", 1)));

				player_counter++;
				player_counter %= 3;

			}

			if (left > -3)
				left--;
		}

		this.ctrl_queue.enqueue(new CtrlInitializeCommand(null, players, field_models));

		// attention this handler should be deprecated

		// this.model.setDefaultMoveEventHandler(new MoveEventHandler() {
		//
		// public void execute(Field from, Field to) {
		//
		// CtrlMoveCommand move = new CtrlMoveCommand(from, to);
		// move.setViewCommandQueue(view_command_queue);
		// move.run();
		//
		// }
		//
		// });

		// attention maybe find a better way for this

		// this.model.setUnit(new Point2D(10, 10), "basic-unit");
		// this.model.setUnit(new Point2D(5, 5), "basic-unit");
		// this.model.setUnit(new Point2D(5, 10), "basic-unit");
		// this.model.setUnit(new Point2D(4, 7), "basic-unit");

	}

}
