package server.launcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Launcher {

	private String username = "wdvozwsr";
	private String password = "lzMIQ5SJvR083poynUF6Rc8T_QPNUJow";
	private String hostname = "raven.rmq.cloudamqp.com";

	private String uri = "amqp://" + username + ":" + password + "@" + hostname + "/" + username;

	private String user_request_queue = "user-request-queue";
	private String room_request_queue = "room-request-queue";

	private String user_response_exchange = "user-response-exchange";
	private String room_response_exchange = "room-response-exchange";

	private ConnectionFactory conn_factory;
	private Connection connection;
	private Channel channel;

	private Map<String, List<String>> rooms;
	private Map<String, String> users;

	public Launcher() {

		this.rooms = new HashMap<String, List<String>>();
		this.users = new HashMap<String, String>();

		this.conn_factory = new ConnectionFactory();
		try {
			this.conn_factory.setUri(this.uri);

			this.connection = this.conn_factory.newConnection();
			this.channel = this.connection.createChannel();

			this.channel.exchangeDeclare(this.user_response_exchange, "direct");
			this.channel.exchangeDeclare(this.room_response_exchange, "direct");

			this.channel.queueDeclare(this.user_request_queue, true, false, false, null);

			this.channel.basicConsume(this.user_request_queue, new DefaultConsumer(this.channel) {

				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {

					System.out.println("Received user-request ...");

					String message = new String(body);
					// id # login # username # password
					String[] args = message.split("#");

					String user_response = "ok#logged";

					channel.basicPublish(user_response_exchange, args[0], null, user_response.getBytes());

					if (!users.containsKey(args[2])) {
						users.put(args[2], args[0]);
					}

				}

			});

			this.channel.queueDeclare(this.room_request_queue, true, false, false, null);

			this.channel.basicConsume(this.room_request_queue, new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {

					String[] args = (new String(body)).split("#");

					// id # requestType # roomName # roomPassword # username
					List<String> players = rooms.get(args[2]);

					if (args[1].equalsIgnoreCase("create")) {
						// create room request
						if (players == null) {
							// ok state to create

							players = new ArrayList<String>();
							players.add(args[4]); // username

							rooms.put(args[2], players);

							// TODO just send ack for room creation
							String message = "create-ok#room-created";
							channel.basicPublish(room_response_exchange, args[0], null, message.getBytes());

						} else {
							// room already exists

							// TODO send error message
							String message = "error#room-already-exists";
							channel.basicPublish(room_response_exchange, args[0], null, message.getBytes());

						}

					} else {
						// join room request

						if (players == null) {
							// bad state, there is no room

							// TODO send error message
							String message = "error#bad-room-name";
							channel.basicPublish(room_response_exchange, args[0], null, message.getBytes());

						} else {

							// room exists

							// TODO add to room
							// TODO send room ack
							// TODO send update to all other users
							players.add(args[4]);

							String server_response = "join-ok";

							for (String player : players) {

								String key = users.get(player);
								String message = "user-join#" + args[4];
								channel.basicPublish(room_response_exchange, key, null, message.getBytes());

								server_response += "#" + player;
							}

							channel.basicPublish(room_response_exchange, args[0], null, server_response.getBytes());

						}

					}

				}
			});

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

	public static void main(String[] args) {
		System.out.println("Creating launcher ...");

		Launcher launcher = new Launcher();

		System.out.println("After launcher ...");

	}

}
