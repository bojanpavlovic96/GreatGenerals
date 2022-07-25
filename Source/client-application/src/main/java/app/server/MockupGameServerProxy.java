package app.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.event.MoveModelEventArg;
import proxy.RabbitGameServerProxyConfig;
import root.ActiveComponent;
import root.command.CommandQueue;
import root.communication.GameServerProxy;
import root.communication.MessageInterpreter;
import root.communication.ProtocolTranslator;
import root.model.event.ModelEventArg;

// TODO had to be in this (client-applicatoin) project because of the reference 
// to MoveModelEventArg, it is just a mockup anyways so let it be ... 
public class MockupGameServerProxy implements GameServerProxy, ActiveComponent {

	private RabbitGameServerProxyConfig config;
	private ProtocolTranslator protocolTranslator;
	private MessageInterpreter messageTranslator;
	private String username;
	private String roomName;

	private CommandQueue commandQueue;

	private ExecutorService executor;

	public MockupGameServerProxy(
			RabbitGameServerProxyConfig config,
			ProtocolTranslator protocolTranslator,
			MessageInterpreter messageTranslator,
			String username,
			String roomName) {
		super();

		this.config = config;
		this.protocolTranslator = protocolTranslator;
		this.messageTranslator = messageTranslator;
		this.username = username;
		this.roomName = roomName;

		this.commandQueue = new CommandQueue();
		this.executor = Executors.newFixedThreadPool(1);

	}

	@Override
	public CommandQueue getConsumerQueue() {
		return this.commandQueue;
	}

	@Override
	public boolean sendIntention(ModelEventArg action) {

		var message = messageTranslator.ToMessage(action);
		byte[] byteMessage = protocolTranslator.toByteData(message);
		// let's say this message is gonna be sent

		executor.submit(() -> {

			// simulating receiving message from rabbit's channel thread
			// and posting it to the commandQueue from the thread 
			// that is not application's main thread 

			int startX = (int) ((MoveModelEventArg) action).getSourceField().getX();
			int startY = (int) ((MoveModelEventArg) action).getSourceField().getY();

			int endX = (int) ((MoveModelEventArg) action).getDestinationField().getX();
			int endY = (int) ((MoveModelEventArg) action).getDestinationField().getY();

			// oh look we received a new command
			// String stringCommand = "{\"name\":\"move-command\",\"payload\":\"{ name: move-command, startFieldPos: {x:"
			// 		+ startX + ",y:" + startY + "},  secondFieldPos: {x:" + endX + ",y:" + endY + "} } \"}";

			// oh look we received a new command 
			// this string should represend an actual message received/sent from/to rabbit
			// String stringCommand = "{\"name\":\"move-cmd-msg\",\"payload\":"
			// 		+ "\"{ \"name\":\"move-cmd-msg\",\"roomName\": " + roomName
			// 		+ ",\"player\": " + username + ","
			// 		+ "startFieldPos: {x:" + startX + ",y:" + startY + "},"
			// 		+ "endFieldPos: {x:" + endX + ",y:" + endY + "} } \"}";

			String stringCommand = "{\"name\":\"move-cmd-msg\",\"payload\":"
					+ "\"{ name: move-cmd-msg, roomName: " + roomName
					+ ", player: " + username + ","
					+ "startFieldPos: {x:" + startX + ",y:" + startY + "},"
					+ "endFieldPos: {x:" + endX + ",y:" + endY + "} } \"}";

			var inMessage = protocolTranslator.toMessage(stringCommand.getBytes());
			var command = messageTranslator.ToCommand(inMessage);

			// Command command = messageTranslator.ToCommand(incomingMessage);
			System.out.println("Mockup server proxy queueing: " + command.getName());

			// Command command = new CtrlMoveCommand(
			// ((MoveModelEventArg) action).getSourceField(),
			// ((MoveModelEventArg) action).getDestinationField());

			commandQueue.enqueue(command);

		});

		return true;
	}

	@Override
	public void shutdown() {
		if (executor != null) {
			executor.shutdown();
		}
		System.out.println("Mockup game server proxy shutdown ... ");
	}

}
