package server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Server {

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

			System.out.println("Server channel created ...");

			channel.exchangeDeclare("login-exc", "topic", false);

			channel.basicPublish("login-exc", "orange", null, (new String("some oranges").getBytes()));
			System.out.println("Message published ...");

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
