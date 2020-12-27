package app.launcher;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import app.event.ConnectionEventHandler;
import app.resource_manager.BrokerConfig;
import root.ActiveComponent;

public class ConnectionTask implements Runnable, ActiveComponent {

	private BrokerConfig brokerConfig;

	private ConnectionFactory connFactory;
	private Connection connection;

	private ConnectionEventHandler connectionReadyHandler;
	private ConnectionEventHandler connectionFailedHandler;

	public ConnectionTask(BrokerConfig brokerConfig,
			ConnectionEventHandler connectionReadyHandler,
			ConnectionEventHandler connectionFailedHandler) {

		this.brokerConfig = brokerConfig;
		this.connectionReadyHandler = connectionReadyHandler;
		this.connectionFailedHandler = connectionFailedHandler;

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

				if (this.connectionReadyHandler != null) {

					this.connectionReadyHandler.execute(this);

				}

			} else {
				System.out.println("Connection failed ... @ Launcher.ConnectionThread");

				if (this.getConnectionFailedHandler() != null) {
					this.getConnectionFailedHandler().execute(this);
				}
			}
		} catch (Exception e) {

			// TODO maybe add some logic for retrying to connect
			// maybe list of broker addresses or something like that

			e.printStackTrace();

			System.out.println("Exception in broker connection ..."
					+ " @ Launcher.ConnectionThread");

			if (this.getConnectionFailedHandler() != null) {
				this.getConnectionFailedHandler().execute(this);
			}

		}

		// all possible exceptions
		// } catch (KeyManagementException e) {
		// e.printStackTrace();
		// } catch (NoSuchAlgorithmException e) {
		// e.printStackTrace();
		// } catch (URISyntaxException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	public void shutdown() {

		try {

			// close connection
			if (this.getConnection() != null && this.getConnection().isOpen()) {
				this.getConnection().close();

				// debug
				System.out.println("Closing connection ... @ ConnectionTask.shutdown");
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

			if (this.getConnectionFailedHandler() != null) {
				this.getConnectionFailedHandler().execute(this);
			}

		}

		return null;
	}

	public boolean isConnected() {
		return this.isConnected();
	}

	public ConnectionEventHandler getConnectionReadyHandler() {
		return connectionReadyHandler;
	}

	public void setConnectionReadyHandler(ConnectionEventHandler connectionReadyHandler) {

		this.connectionReadyHandler = connectionReadyHandler;

		if (this.isConnected()) {
			if (this.connectionReadyHandler != null) {
				this.connectionReadyHandler.execute(this);
			}
		}

	}

	public ConnectionEventHandler getConnectionFailedHandler() {
		return connectionFailedHandler;
	}

	public void setConnectionFailedHandler(ConnectionEventHandler connectionFailedHandler) {
		this.connectionFailedHandler = connectionFailedHandler;
	}

}
