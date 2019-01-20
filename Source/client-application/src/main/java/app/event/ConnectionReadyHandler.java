package app.event;

import com.rabbitmq.client.Channel;

public interface ConnectionReadyHandler {
	
	void execute(Channel channel);
	
}
