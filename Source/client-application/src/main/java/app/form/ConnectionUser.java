package app.form;

import com.rabbitmq.client.Channel;

public interface ConnectionUser {
	
	void setCommunicationChannel(Channel channel);
	
	Channel getCommunicationChannel();
	
}
