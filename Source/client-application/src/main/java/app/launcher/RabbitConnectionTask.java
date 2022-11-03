package app.launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownSignalException;

import app.resource_manager.BrokerConfigFields;
import proxy.ConnectionEventHandler;
import proxy.RabbitChannelProvider;
import proxy.RabbitConnectionEventType;
import root.ActiveComponent;

public class RabbitConnectionTask
		implements Runnable, ActiveComponent, RabbitChannelProvider {

	private BrokerConfigFields brokerConfig;

	private ConnectionFactory connFactory;
	private Connection connection;

	private List<ConnectionEventHandler> eventSubscribers;

	public RabbitConnectionTask(BrokerConfigFields brokerConfig) {

		this.eventSubscribers = new ArrayList<ConnectionEventHandler>();

		this.brokerConfig = brokerConfig;
		System.out.println("Using broker config: \n" + brokerConfig.toString());

	}

	public void run() {

		this.connFactory = new ConnectionFactory();

		try {

			this.connFactory.setUsername(this.brokerConfig.username);
			this.connFactory.setPassword(this.brokerConfig.password);
			this.connFactory.setHost(this.brokerConfig.address);

			this.connFactory.setVirtualHost(this.brokerConfig.vhost);

			// debug
			System.out.println("Creating connection ... @ ConnectionTask.run");
			this.connection = this.connFactory.newConnection();

			if (this.connection != null && this.connection.isOpen()) {

				// debug
				System.out.println("Broker connection established ...");

				for (ConnectionEventHandler connectionEventHandler : eventSubscribers) {
					connectionEventHandler.handleConnectionEvent(
							this,
							RabbitConnectionEventType.CONNECTION);
				}

				this.connection.addShutdownListener(this::shutdownHandler);

			} else {
				System.out.println("Connection failed ... @ Launcher.ConnectionThread");

				for (ConnectionEventHandler connectionEventHandler : eventSubscribers) {
					connectionEventHandler.handleConnectionEvent(
							this,
							RabbitConnectionEventType.DISCONNECTION);
				}

			}
		} catch (Exception e) {

			// e.printStackTrace();

			System.out.println("Exception in broker connection ..."
					+ " @ Launcher.ConnectionThread -> "
					+ "Error: " + e.getMessage());

			for (ConnectionEventHandler connectionEventHandler : eventSubscribers) {
				connectionEventHandler.handleConnectionEvent(
						this,
						RabbitConnectionEventType.DISCONNECTION);
			}

		}

		// possible exceptions
		// KeyManagementException
		// NoSuchAlgorithmException
		// URISyntaxException
		// IOException

	}

	private void shutdownHandler(ShutdownSignalException cause) {
		System.out.println("Handling connection shutdown in rabbitConnectionTask ... ");
		System.out.println("Shutdown casue: " + cause.getMessage());
		for (var connectionEventHandler : eventSubscribers) {
			connectionEventHandler.handleConnectionEvent(
					this,
					RabbitConnectionEventType.DISCONNECTION);
		}
	}

	public void shutdown() {
		System.out.println("ShuttingDown rabbitConnectionTask ... ");
		try {
			// close connection
			if (this.getConnection() != null && this.getConnection().isOpen()) {
				this.getConnection().close();
			}

			// this thread is free from this moment
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exceptionin closing broker connection ... ");
		}

	}

	public Connection getConnection() {
		return connection;
	}

	@Override // RabbitChannelProvider
	public Channel getChannel() {

		if (isConnected()) {
			System.out.println("Connected with the rabbit, creating channel ... ");

			try {
				return this.connection.createChannel();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Exception in creating new channel ... ");

				return null;
			}

		} else {
			System.out.println("Not connected with the rabbit, cant create channel ... ");
		}

		return null;
	}

	@Override // RabbitChannelProvider
	public boolean isConnected() {
		return (this.connection != null && this.connection.isOpen());
	}

	@Override // RabbitChannelProvider
	public void subscribeForEvents(ConnectionEventHandler newSub) {
		eventSubscribers.add(newSub);
		if (this.isConnected()) {
			newSub.handleConnectionEvent(this, RabbitConnectionEventType.CONNECTION);
		}
	}

	@Override // RabbitChannelProvider
	public void unsubscribeForEvents(ConnectionEventHandler sub) {
		eventSubscribers.remove(sub);
	}

}
