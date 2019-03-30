package server.connection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class ConnectionUnit implements ConnectionTask {

	private boolean connection_established = false;

	private String connection_uri;

	private ConnectionFactory connection_factory;
	private Connection connection;

	private List<Channel> channels;

	private Runnable on_connection;

	public ConnectionUnit(String uri) {
		this.connection_uri = uri;

		this.channels = new ArrayList<Channel>();

	}

	public ConnectionUnit(String uri, Runnable on_connection) {
		this(uri);

		this.on_connection = on_connection;

	}

	@Override
	public void run() {

		this.connection_factory = new ConnectionFactory();

		try {

			this.connection_factory.setUri(this.connection_uri);

			this.connection = this.connection_factory.newConnection();

			if (this.connection != null && this.connection.isOpen()) {

				this.connection_established = true;

				if (this.on_connection != null) {

					this.on_connection.run();

				}

			}

		} catch (KeyManagementException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Channel getChannel() {
		try {
			Channel new_channel = this.connection.createChannel();

			if (new_channel != null & new_channel.isOpen()) {
				this.channels.add(new_channel);

				new_channel.addShutdownListener(new ShutdownListener() {
					@Override
					public void shutdownCompleted(ShutdownSignalException cause) {

						channels.remove(new_channel);

					}
				});
			}

			return new_channel;

		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean connectionEstablished() {
		return this.connection_established;
	}

	@Override
	public void shutdown() {

		// debug
		System.out.println("Shutting down connection unit ... @ ConnectionUnit.shutdown");

		try {

			if (this.channels != null && !this.channels.isEmpty()) {

				// attention when closing channel, on shutdown method is called
				// after channel is removed from the channels list what happens with foreach
				// loop ? ? ?

				for (Channel channel : this.channels) {
					channel.close();
				}

			}

			if (this.connection != null && this.connection.isOpen()) {
				this.connection.close();
			}

		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		this.connection_established = false;

	}

}
