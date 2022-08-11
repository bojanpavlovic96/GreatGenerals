package proxy;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

import root.ActiveComponent;
import root.communication.ProtocolTranslator;
import root.communication.RoomServerProxy;
import root.communication.RoomServerResponseHandler;
import root.communication.messages.CreateRoomRequestMsg;
import root.communication.messages.LeaveRoomRequestMsg;

public class RabbitRoomServerProxy implements RoomServerProxy, ActiveComponent {

	private RabbitRoomServerProxyConfig config;

	private RabbitChannelProvider channelProvider;
	private Channel channel;
	private ProtocolTranslator translator;

	// one request at the time can he sent/handled
	private RoomServerResponseHandler handler;
	private String recvQueue;

	public RabbitRoomServerProxy(RabbitRoomServerProxyConfig config,
			RabbitChannelProvider channelProvider,
			ProtocolTranslator translator) {

		this.config = config;

		this.channelProvider = channelProvider;
		this.translator = translator;

		this.handler = null;
		this.recvQueue = null;

	}

	@Override
	public void CreateRoom(String roomName, String password, String playerName,
			RoomServerResponseHandler handler) {

		if (alreadyInUse()) {
			System.out.println("Rabbit room server proxy is already handling request ...  ");
			return;
		}

		if (channelProvider == null || !channelProvider.isConnected()) {
			System.out.println("Broker channel is not open ... ");
			System.out.println("Cant send createRoom request ... ");
			return;
		}

		try {
			// subscribe for a joinResponse before sending create/join request 

			if (channel == null) {
				channel = channelProvider.getChannel();
			}

			if (channel == null) {
				System.out.println("Channel provider is connected but returned null ... ");
				return;
			}

			channel.exchangeDeclare(config.roomResponseExchange,
					config.rabbitTopicExchangeKeyword,
					false,
					true,
					null);

			recvQueue = channel.queueDeclare().getQueue();
			System.out.println("Will receive on route: "
					+ formRoute(config.roomResponseRoutePrefix, roomName, playerName));

			channel.queueBind(recvQueue,
					config.roomResponseExchange,
					formRoute(config.roomResponseRoutePrefix, roomName, playerName)
			// "#"
			);

			this.handler = handler;

			var consumer = new RoomResponseConsumer(this, handler, translator);
			channel.basicConsume(recvQueue, consumer);

			// actually send createRoomRequest
			channel.exchangeDeclare(config.newRoomRequestExchange,
					config.rabbitTopicExchangeKeyword,
					false,
					true,
					null);

			var message = new CreateRoomRequestMsg(roomName, password, playerName);
			var byteMsg = translator.toByteData(message);

			channel.basicPublish(config.newRoomRequestExchange,
					formRoute(config.newRoomRequestRoutePrefix, roomName, playerName),
					null,
					byteMsg);

			System.out.println("Topic: " + config.newRoomRequestExchange +
					" Route: " + formRoute(config.newRoomRequestRoutePrefix, roomName, playerName));

			System.out.println("Create room request successfully sent ... ");
			System.out.println(translator.toStrData(message));

		} catch (Exception e) {
			System.out.println("Exception while creating room ... ");
			e.printStackTrace();
			System.out.println(e.getMessage());

			return;
		}

	}

	private String formRoute(String prefix, String room, String user) {
		return prefix + room + "." + user;
	}

	@Override
	public void JoinRoom(String roomName, String password, String playerName,
			RoomServerResponseHandler handler) {

	}

	@Override
	public void LeaveRoom(String roomName, String username, RoomServerResponseHandler handler) {

		if (alreadyInUse()) {
			System.out.println("Rabbit room server proxy is already handling request ...  ");
			return;
		}

		if (channelProvider == null || !channelProvider.isConnected()) {
			System.out.println("Broker channel is not open ... ");
			System.out.println("Cant send createRoom request ... ");
			return;
		}

		try {
			// subscribe for a joinResponse before sending create/join request 

			if (channel == null) {
				channel = channelProvider.getChannel();
			}

			if (channel == null) {
				System.out.println("Channel provider is connected but returned null ... ");
				return;
			}

			channel.exchangeDeclare(config.roomResponseExchange,
					config.rabbitTopicExchangeKeyword,
					false,
					true,
					null);

			recvQueue = channel.queueDeclare().getQueue();
			var recvRoute = formRoute(config.roomResponseRoutePrefix, roomName, username));
			System.out.println("Will receive on route: " + recvRoute);

			channel.queueBind(recvQueue,
					config.roomResponseExchange,
					recvRoute);

			this.handler = handler;

			var consumer = new RoomResponseConsumer(this, handler, translator);
			channel.basicConsume(recvQueue, consumer);

			// actually send createRoomRequest
			channel.exchangeDeclare(config.leaveRoomRequestExchange,
					config.rabbitTopicExchangeKeyword,
					false,
					true,
					null);

			var message = new LeaveRoomRequestMsg(roomName, username);
			var byteMsg = translator.toByteData(message);

			var sendRoute = formRoute(config.leaveRoomRequestRoutePrefix, roomName, username);
			channel.basicPublish(config.leaveRoomRequestExchange,
					sendRoute,
					null,
					byteMsg);

			System.out.println("Topic: " + config.leaveRoomRequestExchange +
					" Route: " + sendRoute);

			System.out.println("Leave room request successfully sent ... ");
			System.out.println(translator.toStrData(message));

		} catch (Exception e) {
			System.out.println("Exception while leaving room ... ");
			e.printStackTrace();
			System.out.println(e.getMessage());

			return;
		}

	}

	public void clearHandlers() {
		System.out.println("Clearing roomServerProxy ... ");
		handler = null;
		try {
			if (channel != null) {
				channel.queueDelete(recvQueue);
			}

		} catch (Exception e) {
			System.out.println("Exception while trying to clear roomServerProxy ... ");
			System.out.println(e.getMessage());
		}
		// if exception happens it's questionable if channel is gonna be closed 
		// this is kinda ... "channel leak" problem
		channel = null;
		recvQueue = null;

		System.out.println("RabbitRoomServerProxy ready to be used again ... ");
		return;
	}

	@Override

	public boolean alreadyInUse() {
		return (handler != null || recvQueue != null);
	}

	@Override
	public boolean isReady() {
		return channelProvider != null
				&& channelProvider.isConnected()
				&& !alreadyInUse();
	}

	@Override
	public void shutdown() {

		if (channel != null) {
			try {
				if (recvQueue != null) {
					channel.queueDelete(recvQueue);
				}
				channel.close();
			} catch (IOException | TimeoutException e) {
				System.out.println("Exception while closing rabbit channel ... ");
				System.out.println(e.getMessage());

				return;
			}
		}
	}

}
