package server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.AMQP.BasicProperties;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Client {

	public static void main(String[] args) {

		String password = "lzMIQ5SJvR083poynUF6Rc8T_QPNUJow";
		String username = "wdvozwsr";
		String hostname = "raven.rmq.cloudamqp.com";
		String uri = "amqp://" + username + ":" + password + "@" + hostname + "/" + username;

		ConnectionFactory conn_factory = new ConnectionFactory();
		try {
			conn_factory.setUri(uri);
			Connection connection = conn_factory.newConnection();

			Channel channel = connection.createChannel();

			System.out.println("Login server channel created ...");

			channel.exchangeDeclare("login-exc", "topic", false);

			String queue_name = channel.queueDeclare().getQueue();

			channel.queueBind(queue_name, "login-exc", "login");
			channel.basicConsume(queue_name, new DefaultConsumer(channel) {

				@Override
				public void handleDelivery(	String consumerTag,
											Envelope envelope,
											BasicProperties properties,
											byte[] body)
						throws IOException {

					String message = new String(body);

					System.out.println("Handling message: " + message);

				}

			});

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

}
