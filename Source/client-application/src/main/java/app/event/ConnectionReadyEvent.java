package app.event;

import com.rabbitmq.client.Channel;

public interface ConnectionReadyEvent {
	
	void execute(Channel channel);
	
}
