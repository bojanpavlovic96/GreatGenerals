package proxy;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import root.command.CommandQueue;
import root.communication.GameServerProxy;
import root.communication.MsgToCmdTranslator;
import root.communication.ProtocolTranslator;
import root.model.event.ModelEventArg;

public class RabbitGameServerProxy extends DefaultConsumer implements GameServerProxy {

	private RabbitServerProxyConfig config;

	private Channel channel;
	private ProtocolTranslator protocolTranslator;
	private MsgToCmdTranslator msgToCmdTranslator;
	private String username;
	private String roomName;

	private CommandQueue commandQueue;

	private String rcvQueueName;

	public RabbitGameServerProxy(
			RabbitServerProxyConfig config,
			Channel channel,
			ProtocolTranslator translator,
			MsgToCmdTranslator msgToCmdTranslator,
			String username,
			String roomName) {

		super(channel);

		// TODO instead of using username and the roomName
		// pass some token/key received from the server after login/register

		this.config = config;

		this.channel = channel;
		this.protocolTranslator = translator;
		this.msgToCmdTranslator = msgToCmdTranslator;
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
					config.modelEventsExchange,
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
		return config.modelEventsRoutePrefix + "." + roomName + "." + username;
	}

	@Override
	public CommandQueue getConsumerQueue() {
		return this.commandQueue;
	}

	@Override
	public void sendIntention(ModelEventArg modelEvent) {
		var message = msgToCmdTranslator.ToMessage(modelEvent);
		message.setOrigin(this.username, this.roomName);

		byte[] bytePayload = protocolTranslator.toByteData(message);

		try {
			channel.basicPublish(
					config.modelEventsExchange,
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
	public void handleDelivery(String consumerTag,
			Envelope envelope,
			BasicProperties properties,
			byte[] body) throws IOException {

		var newMessage = this.protocolTranslator.toMessage(new String(body));
		if (newMessage == null) {
			// debug
			System.out.println("Failed to translate message ... ");
			return;
		}

		var newCommand = msgToCmdTranslator.ToCommand(newMessage);
		if (newCommand == null) {
			// debug
			System.out.println("Failed to translate message to command ... ");
			return;
		}

		this.commandQueue.enqueue(newCommand);
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
}
