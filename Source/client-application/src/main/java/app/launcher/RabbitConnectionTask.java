package app.launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import app.event.ConnectionEventHandler;
import app.resource_manager.BrokerConfig;
import root.ActiveComponent;

public class RabbitConnectionTask implements Runnable, ActiveComponent {

	private BrokerConfig brokerConfig;

	private ConnectionFactory connFactory;
	private Connection connection;

	private List<ConnectionEventHandler> eventSubscribers;

	public RabbitConnectionTask(BrokerConfig brokerConfig) {

		this.eventSubscribers = new ArrayList<ConnectionEventHandler>();

		this.brokerConfig = brokerConfig;

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

			} else {
				System.out.println("Connection failed ... @ Launcher.ConnectionThread");

				for (ConnectionEventHandler connectionEventHandler : eventSubscribers) {
					connectionEventHandler.handleConnectionEvent(
							this,
							RabbitConnectionEventType.DISCONNECTION);
				}

			}
		} catch (Exception e) {

			e.printStackTrace();

			System.out.println("Exception in broker connection ..."
					+ " @ Launcher.ConnectionThread");

			for (ConnectionEventHandler connectionEventHandler : eventSubscribers) {
				connectionEventHandler.handleConnectionEvent(
						this,
						RabbitConnectionEventType.DISCONNECTION);
			}

		}

		// possible exceptions
		// KeyManagementException
		// NoSuchAlhoritmException
		// URISyntaxException
		// IOException

	}

	public void shutdown() {

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

	public Channel getChannel() {

		try {

			return this.connection.createChannel();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception in creating new channel ... ");

			return null;
		}

	}

	public boolean isConnected() {
		return this.isConnected();
	}

	public void subscribeForEvents(ConnectionEventHandler newSub) {
		this.eventSubscribers.add(newSub);
		if (this.isConnected()) {
			newSub.handleConnectionEvent(this, RabbitConnectionEventType.CONNECTION);
		}
	}

	public void unsubscribeForEvents(ConnectionEventHandler sub) {
		this.eventSubscribers.remove(sub);
	}

}
