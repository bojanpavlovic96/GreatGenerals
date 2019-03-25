package app.launcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import app.event.ConnectionReadyHandler;
import root.ActiveComponent;

public class ConnectionTask implements Runnable, ActiveComponent {

	private boolean connection_established = false;

	private String connection_id;

	private String connection_uri;

	private ConnectionFactory conn_factory;
	private Connection connection;
	private Channel channel;

	private ConnectionReadyHandler on_connection_ready;

	public ConnectionTask(String uri) {

		this.connection_uri = uri;

		this.connection_id = Integer.toString((int) Math.random());
	}

	public void run() {

		this.conn_factory = new ConnectionFactory();

		try {

			this.conn_factory.setUri(this.connection_uri);

			// debug
			System.out.println("Creating connection ... @ ConnectionTask.run");
			this.connection = this.conn_factory.newConnection();

			if (this.getConnection().isOpen()) {

				this.channel = this.getConnection().createChannel();

				if (this.channel != null && this.channel.isOpen()) {

					// debug
					System.out.println("Connection established ...");
					System.out.println("MQ address: " + this.connection.getAddress());

					// debug
					System.out.println("Channel num: " + this.channel.getChannelNumber());

					if (this.on_connection_ready != null) {

						// debug
						System.out.println("Executing on_connection_handler ... @ Launcher.ConnectionThread");

						this.connection_established = true;

						this.on_connection_ready.execute(this.channel);
					}

				}
			}

		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {

		try {

			// close channel
			if (this.getChannel() != null && this.getChannel().isOpen()) {
				this.getChannel().close();

				// debug
				System.out.println("Closing channel ... @ ConnectionTask.shutdown");
			}

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
		return channel;
	}

	public ConnectionReadyHandler getOn_connection_ready() {
		return on_connection_ready;
	}

	public void setOnConnectionReady(ConnectionReadyHandler on_connection_ready) {
		this.on_connection_ready = on_connection_ready;
	}

	public String getConnectionId() {
		return this.connection_id;
	}

	public boolean isConnected() {
		return this.connection_established == true;
	}

}
