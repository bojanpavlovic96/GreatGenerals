package server.service;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

import server.data.UserData;
import server.database.Database;

public class DefaultLoginService implements LoginService {

	// TODO create queue naming manager
	private static String EXC_NAME = "login-exc";
	private static String REQUEST_ROUTING_KEY = "login-request";
	private static String RESPONSE_ROUTING_KEY = "login-response";

	private Channel channel;
	private Database database;

	private String request_queue_name;

	public DefaultLoginService(Channel channel, Database database) {
		this.channel = channel;
		this.database = database;

		this.initializeChannel();

	}

	private String generateQueryFor(String username) {
		return "select all from users where username=" + username;
	}

	private void initializeChannel() {

		try {

			this.channel.exchangeDeclare(DefaultLoginService.EXC_NAME, "topic");

			this.request_queue_name = this.channel.queueDeclare().getQueue();
			this.channel.queueBind(	this.request_queue_name,
									DefaultLoginService.EXC_NAME,
									DefaultLoginService.REQUEST_ROUTING_KEY);

			this.channel.basicConsume(this.request_queue_name, new DefaultConsumer(this.channel) {

				@Override
				public void handleDelivery(	String consumerTag,
											Envelope envelope,
											BasicProperties properties,
											byte[] body)
						throws IOException {

					String request = new String(body);

					System.out.println("New login request: " + request);

				}

			});

		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public UserData checkUser(String username, String password) {

		return null;
	}

	@Override
	public void setCommunicationChannel(Channel channel) {
		this.channel = channel;
		this.initializeChannel();
	}

	@Override
	public Channel getCommunicationChannel() {
		return this.channel;
	}

}
