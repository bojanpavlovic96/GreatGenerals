package app.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.event.MoveModelEventArg;
import proxy.RabbitServerProxyConfig;
import root.ActiveComponent;
import root.command.Command;
import root.command.CommandQueue;
import root.communication.GameServerProxy;
import root.communication.Translator;
import root.model.event.ModelEventArg;

// TODO had to be in this (client-applicatoin) project because of the reference 
// to MoveModelEventArg, it is just a mockup anyways so let it be ... 
public class MockupGameServerProxy implements GameServerProxy, ActiveComponent {

	private RabbitServerProxyConfig config;
	private Translator translator;
	private String username;
	private String roomName;

	private CommandQueue commandQueue;

	private ExecutorService executor;

	public MockupGameServerProxy(
			RabbitServerProxyConfig config,
			Translator translator,
			String username,
			String roomName) {
		super();

		this.config = config;
		this.translator = translator;
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
	public void sendIntention(ModelEventArg action) {

		byte[] message = translator.toByteData(action);
		// let's say this message is gonna be sent

		executor.submit(() -> {

			// simulating receiving message from rabbit's channel thread
			// and posting it to the commandQueue from the thread 
			// that is not application's main thread 

			int startX = (int) ((MoveModelEventArg) action).getSourceField().getX();
			int startY = (int) ((MoveModelEventArg) action).getSourceField().getY();

			int endX = (int) ((MoveModelEventArg) action).getDestinationField().getX();
			int endY = (int) ((MoveModelEventArg) action).getDestinationField().getY();

			// oh look we received new command
			String stringCommand = "{\"name\":\"move-command\",\"payload\":\"{ name: move-command, startFieldPos: {x:"
					+ startX + ",y:" + startY + "},  secondFieldPos: {x:" + endX + ",y:" + endY + "} } \"}";

			Command command = translator.toCommand(stringCommand);
			System.out.println("Mockup server proxy queueing: " + command.getName());

			// Command command = new CtrlMoveCommand(
			// ((MoveModelEventArg) action).getSourceField(),
			// ((MoveModelEventArg) action).getDestinationField());

			commandQueue.enqueue(command);

		});

	}

	@Override
	public void shutdown() {
		if (executor != null) {
			executor.shutdown();
		}
		System.out.println("Mockup game server proxy shutdown ... ");
	}

}
