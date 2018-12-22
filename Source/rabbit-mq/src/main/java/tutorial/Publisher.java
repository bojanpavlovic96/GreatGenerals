package tutorial;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher {
	public static void main(String[] args) throws InterruptedException {

		try {

			ConnectionFactory conn_factory = new ConnectionFactory();
			conn_factory.setUri("amqp://guest:guest@localhost");
			conn_factory.setConnectionTimeout(300000);

			Connection connection = conn_factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare("my-queue", true, false, false, null);

			int count = 0;
			while (count < 5000) {
				String message = "Message number" + count;

				channel.basicPublish("", "my-queue", null, message.getBytes());
				count++;
				System.out.println("Published message: " + message);

				Thread.sleep(1000);

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
}
