package proxy;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import root.command.CommandQueue;
import root.communication.GameServerProxy;
import root.communication.Translator;
import root.model.event.ModelEventArg;

public class RabbitGameServerProxy extends DefaultConsumer implements GameServerProxy {

	private RabbitServerProxyConfig config;

	private Channel channel;
	private Translator translator;
	private String username;
	private String roomName;

	private CommandQueue commandQueue;

	private String rcvQueueName;

	public RabbitGameServerProxy(
			RabbitServerProxyConfig config,
			Channel channel,
			Translator translator,
			String username,
			String roomName) {

		super(channel);

		// TODO instead of using username and the roomName
		// pass some token/key received from the server after login/register

		this.config = config;

		this.channel = channel;
		this.translator = translator;
		this.username = username;
		this.roomName = roomName;

		this.commandQueue = new CommandQueue();

		this.subscribeToServerCommands();
	}

	private void subscribeToServerCommands() {
		if (channel == null || !channel.isOpen()) {
			// TODO throw some exception ...
			// debug
			System.out.println("Broker channel is not open ... ");
			System.out.println("Cant create game server proxy ... ");
			return;
		}

		try {
			channel.exchangeDeclare(
					config.serverCommandsExchange,
					config.rabbitTopicExchangeKeyword);

			channel.exchangeDeclare(
					config.clientEventsExchange,
					config.rabbitTopicExchangeKeyword);

			rcvQueueName = channel.queueDeclare().getQueue();
			channel.queueBind(
					rcvQueueName,
					config.serverCommandsExchange,
					genCommandRoutingKey());

			channel.basicConsume(rcvQueueName, this);

		} catch (IOException e) {
			e.printStackTrace();
			// debug
			System.out.println("Exception in rabbitServerProxy channel initialization ... ");
			System.out.println("Exc message: " + e.getMessage());
			return;
		}

	}

	private String genCommandRoutingKey() {
		return config.serverCommandsRoutePrefix + "." + roomName + "." + username;
	}

	private String genEventRoutingKey() {
		return config.clientEventsRoutePrefix + "." + roomName + "." + username;
	}

	@Override
	public void setConsumerQueue(CommandQueue consumerQueue) {
		this.commandQueue = consumerQueue;
		// TODO but this does not look good
		// better remove this method from the interface
	}

	@Override
	public CommandQueue getConsumerQueue() {
		return this.commandQueue;
	}

	@Override
	public void sendIntention(ModelEventArg modelEvent) {
		byte[] bytePayload = translator.toByteData(modelEvent);

		try {
			channel.basicPublish(
					config.clientEventsExchange,
					genEventRoutingKey(),
					null,
					bytePayload);

		} catch (IOException e) {
			e.printStackTrace();
			// debug
			System.out.println("Failed to publish client event: "
					+ modelEvent.getClass().getName());
			System.out.println("Exc message: " + e.getMessage());
			return;
		}
	}

	// region Consumer implementation

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body)
			throws IOException {

		var newCommand = this.translator.toCommand(new String(body));
		if (newCommand != null) {
			// TODO if name is uknown translator will return null
			// this could be exception as well ...
			this.commandQueue.enqueue(newCommand);
		}

	}

	// @Override
	// public void handleConsumeOk(String consumerTag) {

	// }

	// @Override
	// public void handleCancelOk(String consumerTag) {

	// }

	// @Override
	// public void handleCancel(String consumerTag) throws IOException {

	// }

	// @Override
	// public void handleShutdownSignal(String consumerTag, ShutdownSignalException
	// sig) {

	// }

	// @Override
	// public void handleRecoverOk(String consumerTag) {

	// }

	// endregion

	// fake initialization saved from the previous version

	// private void initCommunicationChannel() {
	// // configure channels and start listening for server messages

	// // first message must be model initialization message

	// // attention do not start receiving messages until queue is set !!!

	// // fake first initialization message
	// List<PlayerData> players = new ArrayList<PlayerData>();
	// players.add(new PlayerModelData("user 1", Color.RED));
	// players.add(new PlayerModelData("user 2", Color.GREEN));
	// players.add(new PlayerModelData("user 3", Color.BLACK));

	// List<Field> fieldModels = new ArrayList<Field>();

	// int left = 3;
	// int right = 17;

	// int playerCounter = 0;

	// for (int i = 1; i < 16; i++) {

	// for (int j = left; j < right; j++) {
	// if (i % 2 == 0 && j % 5 == 0) {
	// fieldModels.add(new ModelField(
	// new Point2D(j, i),
	// players.get(playerCounter),
	// true,
	// null,
	// new Terrain("mountains", 1)));
	// } else {
	// fieldModels.add(new ModelField(
	// new Point2D(j, i),
	// players.get(playerCounter),
	// true,
	// null,
	// new Terrain("water", 1)));
	// }

	// playerCounter++;
	// playerCounter %= 3;

	// }

	// if (left > -3)
	// left--;
	// }

	// this.commandQueue.enqueue(new CtrlInitializeCommand(players, fieldModels));

	// }

}
