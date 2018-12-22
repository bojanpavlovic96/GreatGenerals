package tutorial;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Consumer {
	public static void main(String[] args) {

		try {
			ConnectionFactory conn_fact = new ConnectionFactory();
			conn_fact.setUri("amqp://guest:guest@localhost");
			conn_fact.setConnectionTimeout(300000);

			Connection connection = conn_fact.newConnection();

			Channel channel = connection.createChannel();

			channel.queueDeclare("my-queue", true, false, false, null);

			QueueingConsumer consumer = new QueueingConsumer(channel);

			// start consuming messages from queue channel
			channel.basicConsume("my-queue", false, consumer);

			while (true) {

				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				if (delivery != null) {
					try {
						String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

						System.out.println("Message consumed: " + message);
						channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
					} catch (Exception e) {
						channel.basicReject(delivery.getEnvelope().getDeliveryTag(), true);
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
		} catch (ShutdownSignalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
