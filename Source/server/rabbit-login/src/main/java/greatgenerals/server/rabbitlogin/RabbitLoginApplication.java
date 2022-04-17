package greatgenerals.server.rabbitlogin;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import greatgenerals.server.rabbitlogin.receiver.LoginReceiver;

@SpringBootApplication
public class RabbitLoginApplication {

	static final String TOPIC_EXCHANGE_NAME = "spring-boot-exchange";
	static final String QUEUE_NAME = "spring-boot";

	@Bean
	public Queue queue() {
		return new Queue(QUEUE_NAME, false);
	}

	// @Bean
	// public Queue random_queue() {
	// 	return new Queue("randomqueue", false);
	// }

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(TOPIC_EXCHANGE_NAME);
	}

	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("");
	}

	// @Bean
	// SimpleMessageListenerContainer container(
	// ConnectionFactory connectionFactory,
	// MessageListenerAdapter listenerAdapter) {

	// var container = new SimpleMessageListenerContainer();
	// container.setConnectionFactory(connectionFactory);
	// container.setQueueNames(QUEUE_NAME);
	// container.setMessageListener(listenerAdapter);
	// return container;
	// }

	// @Bean
	// MessageListenerAdapter listenerAdapter(LoginReceiver receiver) {
	// return new MessageListenerAdapter(
	// receiver,
	// "receiveMessage");
	// }

	public static void main(String[] args) {
		SpringApplication.run(RabbitLoginApplication.class, args);
	}

}
