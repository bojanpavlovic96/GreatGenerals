package proxy;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

import root.ActiveComponent;
import root.communication.ProtocolTranslator;
import root.communication.RoomServerProxy;
import root.communication.RoomServerResponseHandler;
import root.communication.messages.CreateRoomRequestMsg;
import root.communication.messages.JoinRoomRequestMsg;
import root.communication.messages.LeaveRoomRequestMsg;
import root.communication.messages.Message;
import root.communication.messages.StartGameRequestMsg;

public class RabbitRoomServerProxy implements RoomServerProxy, ActiveComponent {

	private RabbitRoomServerProxyConfig config;

	private RabbitChannelProvider channelProvider;
	private Channel channel;
	private ProtocolTranslator translator;

	// one request at the time can he sent/handled
	private RoomServerResponseHandler responseHandler;
	private String responseQueue;

	private RoomServerResponseHandler updateHandler;
	private String updateQueue;

	public RabbitRoomServerProxy(RabbitRoomServerProxyConfig config,
			RabbitChannelProvider channelProvider,
			ProtocolTranslator translator) {

		this.config = config;

		this.channelProvider = channelProvider;
		this.translator = translator;

		this.responseHandler = null;
		this.responseQueue = null;

	}

	private boolean setupReceiver(String roomName, String playerName,
			RoomServerResponseHandler handler) {

		if (channel == null || !channel.isOpen()) {
			channel = channelProvider.getChannel();
		}

		if (channel == null) {
			System.out.println("Channel provider is connected but returned null ... ");
			return false;
		}

		try {

			channel.exchangeDeclare(config.roomResponseExchange,
					config.rabbitTopicExchangeKeyword,
					false,
					true,
					null);

			responseQueue = channel.queueDeclare().getQueue();
			var recvRoute = formResponseRoute(roomName, playerName);
			System.out.println("Will receive on route: " + recvRoute);

			channel.queueBind(responseQueue,
					config.roomResponseExchange,
					recvRoute);

			this.responseHandler = handler;

			var consumer = new RoomResponseConsumer(this, responseHandler, translator);
			channel.basicConsume(responseQueue, consumer);

		} catch (IOException e) {
			System.out.println("Got exception while setting up room response receiver ... ");
			System.out.println(e.getMessage());
			e.printStackTrace();

			return false;
		}

		return true;
	}

	@Override
	public void CreateRoom(String roomName, String password, String playerName,
			RoomServerResponseHandler handler) {

		if (isWaitingForResponse()) {
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

			var setupResult = setupReceiver(roomName, playerName, handler);
			if (!setupResult) {
				System.out.println("Failed to setup receiver ... ");
				return;
			}

			// actually send createRoomRequest

			sendRequest(new CreateRoomRequestMsg(new Date(), roomName, password, playerName),
					config.newRoomRequestExchange,
					formCreateRequestRoute(roomName, playerName));

		} catch (Exception e) {
			System.out.println("Exception while sending create room request ... ");
			e.printStackTrace();
			System.out.println(e.getMessage());

			return;
		}

	}

	private String formCreateRequestRoute(String room, String user) {
		return config.newRoomRequestRoutePrefix + room + "." + user;
	}

	@Override
	public void JoinRoom(String roomName, String password, String playerName,
			RoomServerResponseHandler handler) {

		if (isWaitingForResponse()) {
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

			var setupResult = setupReceiver(roomName, playerName, handler);
			if (!setupResult) {
				System.out.println("Failed to setup receiver ... ");
				return;
			}

			// actually send joinRoomRequest

			sendRequest(new JoinRoomRequestMsg(new Date(), roomName, password, playerName),
					config.joinRoomRequestExchange,
					formJoinRequestRoute(roomName, playerName));

		} catch (Exception e) {
			System.out.println("Exception while sending join room request ... ");
			e.printStackTrace();
			System.out.println(e.getMessage());

			return;
		}

	}

	private String formJoinRequestRoute(String room, String user) {
		return config.joinRoomRequestRoutePrefix + room + "." + user;
	}

	@Override
	public void LeaveRoom(String roomName, String playerName, RoomServerResponseHandler handler) {

		if (isWaitingForResponse()) {
			System.out.println("Rabbit room server proxy is already handling request ...  ");
			return;
		}

		if (channelProvider == null || !channelProvider.isConnected()) {
			System.out.println("Broker channel is not open ... ");
			System.out.println("Cant send createRoom request ... ");
			return;
		}

		try {
			// subscribe for a leave before sending create/join request 

			var setupResult = setupReceiver(roomName, playerName, handler);
			if (!setupResult) {
				System.out.println("Failed to setup receiver ... ");
				return;
			}

			// actually send leaveRoomRequest

			sendRequest(new LeaveRoomRequestMsg(new Date(), roomName, playerName),
					config.leaveRoomRequestExchange,
					formLeaveRequestRoute(roomName, playerName));

		} catch (Exception e) {
			System.out.println("Exception while leaving room ... ");
			e.printStackTrace();
			System.out.println(e.getMessage());

			return;
		}

	}

	private String formLeaveRequestRoute(String room, String user) {
		return config.leaveRoomRequestRoutePrefix + room + "." + user;
	}

	@Override
	public void StartGame(String roomName, String username, RoomServerResponseHandler handler) {

		if (isWaitingForResponse()) {
			System.out.println("Rabbit room server proxy is already handling request ...  ");
			return;
		}

		if (channelProvider == null || !channelProvider.isConnected()) {
			System.out.println("Broker channel is not open ... ");
			System.out.println("Cant send createRoom request ... ");
			return;
		}

		var setupResult = setupReceiver(roomName, username, handler);
		if (!setupResult) {
			System.out.println("Failed to setup startGame response receiver ... ");
			return;
		}

		try {

			sendRequest(new StartGameRequestMsg(new Date(), roomName, username),
					config.startGameRequestExchange,
					formStartGameRoute(roomName, username));

		} catch (IOException e) {
			System.out.println("Exception while sending start game request ... ");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	private String formStartGameRoute(String roomName, String playerName) {
		return config.startGameRequestRoutePrefix + roomName + "." + playerName;
	}

	@Override
	public void SubscribeForRoomUpdates(String roomName, String username,
			RoomServerResponseHandler handler) {

		if (channelProvider == null || !channelProvider.isConnected()) {
			System.out.println("Broker channel is not open ... ");
			System.out.println("Cant subscribe for room updates ... ");
			return;
		}

		if (channel == null) {
			channel = channelProvider.getChannel();
		}

		try {

			var topic = config.roomResponseExchange;
			var route = formUpdateRoute(roomName, username);

			channel.exchangeDeclare(topic,
					config.rabbitTopicExchangeKeyword,
					false,
					true,
					null);

			updateQueue = channel.queueDeclare().getQueue();
			channel.queueBind(updateQueue, topic, route);

			this.updateHandler = handler;

			var consumer = new RoomUpdateConsumer(this, updateHandler, translator);
			channel.basicConsume(updateQueue, consumer);

			System.out.println("Subscribed for room updates ... ");
			System.out.println("\tTopic: " + topic);
			System.out.println("\tRoute: " + route);

		} catch (IOException e) {
			System.out.println("EXC while subscribing for room updates ... ");
			System.out.println("EXC: " + e.getMessage());
			return;
		}

	}

	@Override
	public void UnsubFromRoomUpdates() {
		if (updateQueue != null) {
			if (channel != null && channel.isOpen()) {
				try {
					channel.queueDelete(updateQueue);
				} catch (IOException e) {
					System.out.println("Exception while trying to delete roomUpdateQueue");
					System.out.println("Exc: " + e.getMessage());
				}
				updateQueue = null;
			}

			this.updateHandler = null;
		}

	}

	private String formUpdateRoute(String room, String user) {
		return config.roomUpdateRoutePrefix + room + "." + user;
	}

	private String formResponseRoute(String room, String user) {
		return config.roomResponseRoutePrefix + room + "." + user;
	}

	public void clearResponseAwaiter() {
		System.out.println("Clearing roomServerProxy ... ");
		responseHandler = null;

		try {
			if (channel != null && channel.isOpen()) {
				channel.queueDelete(responseQueue);
			}

			// channel.close();
		} catch (Exception e) {
			System.out.println("Exception while trying to clear roomServerProxy ... ");
			System.out.println(e.getMessage());
		}
		// if exception happens it's questionable if channel is gonna be closed 
		// this is kinda ... "channel leak" problem
		// channel = null;
		responseQueue = null;

		System.out.println("RabbitRoomServerProxy is ready to receive responses... ");
		return;
	}

	@Override
	public boolean isWaitingForResponse() {
		return (responseHandler != null || responseQueue != null);
	}

	@Override
	public boolean isReceivingUpdates() {
		return (updateHandler != null || updateQueue != null);
	}

	private void sendRequest(Message message, String topic, String route) throws IOException {

		channel.exchangeDeclare(topic,
				config.rabbitTopicExchangeKeyword,
				false,
				true,
				null);

		var byteMsg = translator.toByteData(message);

		channel.basicPublish(topic, route, null, byteMsg);

		System.out.println(message.type.toString() + " sent => "
				+ " topic: " + topic
				+ " rotue: " + route);
	}

	@Override
	public boolean isReady() {
		return channelProvider != null
				&& channelProvider.isConnected()
				&& !isWaitingForResponse();
	}

	@Override
	public void shutdown() {

		if (channel != null && channel.isOpen()) {

			try {
				if (responseQueue != null) {
					channel.queueDelete(responseQueue);
				}
				if (updateQueue != null) {
					channel.queueDelete(updateQueue);
				}

				channel.close();

			} catch (IOException | TimeoutException e) {
				System.out.println("Exception while closing rabbit channel ... ");
				System.out.println(e.getMessage());

				return;
			} catch (Exception e) {
				System.out.println("Unknown exc while shutting down room proxy ... ");
				System.out.println(e.getMessage());

				return;
			}
		}
	}

}
