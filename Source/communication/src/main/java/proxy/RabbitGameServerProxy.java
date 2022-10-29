package proxy;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import root.ActiveComponent;
import root.command.CommandQueue;
import root.communication.GameServerProxy;
import root.communication.MessageInterpreter;
import root.communication.ProtocolTranslator;
import root.model.event.ModelEventArg;

public class RabbitGameServerProxy implements GameServerProxy,
		Consumer,
		ActiveComponent,
		ConnectionEventHandler {

	private RabbitGameServerProxyConfig config;

	private RabbitChannelProvider channelProvider;

	private MessageInterpreter messageInterpreter;
	private ProtocolTranslator protocolTranslator;

	private String username;
	private String roomName;

	private CommandQueue commandQueue;

	private String rcvQueueName;
	private Channel channel;

	public RabbitGameServerProxy(RabbitGameServerProxyConfig config,
			RabbitChannelProvider channelProvider,
			ProtocolTranslator translator,
			MessageInterpreter msgInterpreter,
			String username,
			String roomName) {

		this.config = config;

		this.channelProvider = channelProvider;
		this.protocolTranslator = translator;
		this.messageInterpreter = msgInterpreter;
		this.username = username;
		this.roomName = roomName;

		this.commandQueue = new CommandQueue();

		this.channelProvider.subscribeForEvents(this);
	}

	@Override
	public void handleConnectionEvent(RabbitChannelProvider channelProvider, RabbitConnectionEventType eventType) {
		if (eventType != RabbitConnectionEventType.CONNECTION) {
			// disconnection
			System.out.println("Received NON-connection event in gameSProxy ... ");
			try {
				if (channel != null) {

					if (rcvQueueName != null) {
						channel.queueDelete(rcvQueueName);
					}

					channel.close();
				}
			} catch (IOException | TimeoutException e) {
				System.out.println("Failed to close channel after DISCONNECT event "
						+ "@RabbitRoomServeProxy ... ");
				System.out.println(e.getMessage());
			}

			rcvQueueName = null;
			channel = null;

			return;
		} else {
			// connection 
			System.out.println("Received CONNECTION event in gameSProxy ... ");
			channel = channelProvider.getChannel();

			try {
				channel.exchangeDeclare(
						config.serverMessageExchange,
						config.rabbitTopicExchangeKeyword,
						false,
						true,
						null);

				channel.exchangeDeclare(
						config.modelEventsExchange,
						config.rabbitTopicExchangeKeyword,
						false,
						true,
						null);

				rcvQueueName = channel.queueDeclare().getQueue();
				channel.queueBind(rcvQueueName,
						config.serverMessageExchange,
						genCommandRoutingKey());

				channel.basicConsume(rcvQueueName, this);

			} catch (IOException e) {
				e.printStackTrace();
				// debug
				System.out.println("Exception in rabbitServerProxy channel initialization ... ");
				System.out.println("Exc message: " + e.getMessage());
				return;
			}

			return;
		}

	}

	private String genCommandRoutingKey() {
		return config.serverMessageRoutePrefix + "." + roomName + "." + username;
	}

	private String genEventRoutingKey() {
		return config.modelEventsRoutePrefix + "." + roomName + "." + username;
	}

	@Override
	public CommandQueue getConsumerQueue() {
		return this.commandQueue;
	}

	@Override
	public boolean sendIntention(ModelEventArg modelEvent) {

		if (channel == null) {
			// Channel is gonna be created at each CONNECTION event 
			// (channelProvider.subscribeForEvents).
			// If channel is null either DISCONNECT event happened
			// or CONNECTION event still didn't happen.

			System.out.println("Failed ot send intention, channel not available ... ");

			return false;
		}

		var message = messageInterpreter.ToMessage(modelEvent);
		message.setOrigin(this.username, this.roomName);

		byte[] bytePayload = protocolTranslator.toByteData(message);

		try {
			channel.basicPublish(config.modelEventsExchange,
					genEventRoutingKey(),
					null,
					bytePayload);

		} catch (IOException e) {
			e.printStackTrace();
			// debug
			System.out.println("Failed to publish client event: "
					+ modelEvent.getClass().getName());
			System.out.println("Exc message: " + e.getMessage());
			return false;
		}

		return true;
	}

	// region Consumer implementation

	@Override
	public void handleDelivery(String consumerTag,
			Envelope envelope,
			BasicProperties properties,
			byte[] body) throws IOException {

		var newMessage = protocolTranslator.toMessage(body);
		if (newMessage == null) {
			// debug
			System.out.println("Failed to translate message ... ");
			return;
		}

		var newCommand = messageInterpreter.ToCommand(newMessage);
		if (newCommand == null) {
			// debug
			System.out.println("Failed to translate message to command ... ");
			return;
		}

		commandQueue.enqueue(newCommand);
	}

	@Override
	public void handleConsumeOk(String consumerTag) {

	}

	@Override
	public void handleCancelOk(String consumerTag) {

	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {

	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

	}

	@Override
	public void handleRecoverOk(String consumerTag) {

	}

	// endregion

	@Override
	public void shutdown() {
		if (channel != null) {
			try {
				channel.close();
			} catch (IOException | TimeoutException e) {
				System.out.println("Exc while closing channel, gameserverproxy ... ");
				System.out.println(e.getMessage());
				return;
			}
		}

	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getRoomName() {
		return roomName;
	}

}
