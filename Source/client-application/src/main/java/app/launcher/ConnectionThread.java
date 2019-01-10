package app.launcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import app.event.ConnectionReadyEvent;
import view.ShouldBeShutdown;

public class ConnectionThread implements Runnable, ShouldBeShutdown {

	private String connection_uri;

	private ConnectionFactory conn_factory;
	private Connection connection;
	private Channel channel;

	private ConnectionReadyEvent on_connection_ready;

	public ConnectionThread(String uri) {

		this.connection_uri = uri;

	}

	public void run() {

		this.conn_factory = new ConnectionFactory();

		try {

			this.conn_factory.setUri(this.connection_uri);

			this.connection = this.conn_factory.newConnection();

			if (this.getConnection().isOpen()) {

				this.channel = this.getConnection().createChannel();

				if (this.channel != null && this.channel.isOpen()) {

					System.out.println("Connection established ...");
					System.out.println("MQ address: " + this.connection.getAddress());

					System.out.println("Channel created ...");
					System.out.println("Channel num: " + this.channel.getChannelNumber());

					if (this.on_connection_ready != null) {

						System.out.println("Executing on_connection_handler ...");
						System.out.println("Launcher->ConnectionThread");

						this.on_connection_ready.execute(this.channel);
					}

				}
			}

		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void shutdown() {

		try {

			// close channel
			if (this.getChannel() != null && this.getChannel().isOpen()) {
				this.getChannel().close();

				System.out.println("Closing channel ...");
			}

			// close connection
			if (this.getConnection() != null && this.getConnection().isOpen()) {
				this.getConnection().close();

				System.out.println("Closing connection ...");
			}
			System.out.println("Launcher->Stop->ConnectionThread->shutdown");

			// this thread is free in this moment

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Connection getConnection() {
		return connection;
	}

	public Channel getChannel() {
		return channel;
	}

	public ConnectionReadyEvent getOn_connection_ready() {
		return on_connection_ready;
	}

	public void setOn_connection_ready(ConnectionReadyEvent on_connection_ready) {
		this.on_connection_ready = on_connection_ready;
	}

}
