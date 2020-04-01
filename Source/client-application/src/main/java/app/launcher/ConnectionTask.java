package app.launcher;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import app.event.ConnectionEventHandler;
import root.ActiveComponent;

public class ConnectionTask implements Runnable, ActiveComponent {

	private String connection_uri;

	private ConnectionFactory conn_factory;
	private Connection connection;

	private ConnectionEventHandler connectionReadyHandler;
	private ConnectionEventHandler connectionFailedHandler;

	public ConnectionTask(String uri) {

		this.connection_uri = uri;

	}

	public ConnectionTask(String uri, ConnectionEventHandler connectionReadyHandler,
			ConnectionEventHandler connectionFailedHandler) {
		this(uri);

		this.connectionReadyHandler = connectionReadyHandler;
		this.connectionFailedHandler = connectionFailedHandler;

	}

	// #region testing new connection

	private BrokerInfo brokerInfo;

	public ConnectionTask(BrokerInfo brokerInfo, ConnectionEventHandler connectionReadyHandler,
			ConnectionEventHandler connectionFailedHandler) {

		this.brokerInfo = brokerInfo;
		this.connectionReadyHandler = connectionReadyHandler;
		this.connectionFailedHandler = connectionFailedHandler;

	}
	// #endregion

	public void run() {

		this.conn_factory = new ConnectionFactory();

		try {

			this.conn_factory.setUsername(this.brokerInfo.Username);
			this.conn_factory.setPassword(this.brokerInfo.Password);
			this.conn_factory.setHost(this.brokerInfo.Hostname);

			// this.conn_factory.setUsername("gp_user");
			// this.conn_factory.setPassword("gp_password");
			// this.conn_factory.setHost("localhost");

			// this.conn_factory.setUri(this.connection_uri);

			// debug
			System.out.println("Creating connection ... @ ConnectionTask.run");
			this.connection = this.conn_factory.newConnection();

			if (this.connection != null && this.connection.isOpen()) {

				// debug
				System.out.println("Connection established ...");
				System.out.println("MQ address: " + this.connection.getAddress());

				if (this.connectionReadyHandler != null) {

					System.out.println("Executing connection ready handler ... @ Launcher.ConnectionThread");

					this.connectionReadyHandler.execute(this);

				}

			} else {
				if (this.getConnectionFailedHandler() != null) {
					System.out.println("Connection failed ... @ Launcher.ConnectionThread");
					this.getConnectionFailedHandler().execute(this);
				}
			}
		} catch (Exception e) {

			// only Exception is catch because there is no special handling for every single
			// exception
			// just print stack trace and call connection failed handler

			// TODO maybe add some logic for retrying to connect
			// maybe list of broker addresses or something like that

			e.printStackTrace();

			System.out.println("Connection failed ... @ Launcher.ConnectionThread");

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
		// } finally {
		//
		// System.out.println("Connection failed ... @ Launcher.ConnectionThread");
		// if (this.getConnectionFailedHandler() != null) {
		// this.getConnectionFailedHandler().execute(this);
		// }
		//
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
