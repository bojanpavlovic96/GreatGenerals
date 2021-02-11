package app.server;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import app.event.ConnectionEventHandler;
import app.launcher.RabbitConnectionEventType;
import app.launcher.RabbitConnectionTask;
import app.resource_manager.QueuesConfig;
import root.communication.LoginServerProxy;
import root.communication.LoginServerResponseHandler;
import root.communication.messages.LoginRequest;
import root.communication.messages.RegisterRequest;

public class RabbitLoginServerProxy
		implements LoginServerProxy,
		ConnectionEventHandler {

	private RabbitConnectionTask rabbitConnection;
	private QueuesConfig queuesConfig;

	private String responseQueue;

	public RabbitLoginServerProxy(RabbitConnectionTask rabbitConnection,
			QueuesConfig queuesConfig) {

		this.rabbitConnection = rabbitConnection;
		this.queuesConfig = queuesConfig;

		this.rabbitConnection.subscribeForEvents(this);
	}

	// loign server proxy methods

	@Override
	public boolean isReady() {
		return this.rabbitConnection.isConnected();
	}

	@Override
	public void login(LoginRequest request, LoginServerResponseHandler handler) {

		Channel channel = this.rabbitConnection.getChannel();
		if (channel != null && channel.isOpen()) {

			try {
				// debug
				System.out.println("Publishing on: "
						+ queuesConfig.loginExchange
						+ " for: "
						+ queuesConfig.loginRoutingKey);

				ObjectMapper mapper = new ObjectMapper();
				String strRequest = mapper.writeValueAsString(request);

				channel.basicPublish(
						queuesConfig.loginExchange,
						queuesConfig.loginRoutingKey,
						null,
						strRequest.getBytes());

			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void register(RegisterRequest request, LoginServerResponseHandler handler) {

	}

	// connection event handler methods

	@Override
	public void handleConnectionEvent(
			RabbitConnectionTask connectionTask,
			RabbitConnectionEventType eventType) {

		if (eventType == RabbitConnectionEventType.CONNECTION) {
			this.initChannels();
		} else {
			// do something i guess ...
		}

	}

	private void initChannels() {

		Channel channel = this.rabbitConnection.getChannel();
		if (channel == null || !channel.isOpen()) {
			// TODO maybe show some error message
			return;
		}

		try {

			channel.exchangeDeclare(this.queuesConfig.loginExchange, "topic");

			this.responseQueue = channel.queueDeclare().getQueue();

		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * implement declare queues&exchanges
		 * 
		 * implement set adequate handlers for :
		 * 
		 * login response show adequate message fill this.username and this.password
		 * show roomForm
		 * 
		 * register response show adequate message fill initialPage.username and
		 * initialPage.password
		 * 
		 * room create response show adequate message
		 * 
		 * room join response show adequate message fill playerList
		 * 
		 * startGame response show adequate message call gameReadyHandler
		 * 
		 */

	}

}
