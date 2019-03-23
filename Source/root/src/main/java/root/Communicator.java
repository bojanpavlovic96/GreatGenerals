package root;

import com.rabbitmq.client.Channel;

public interface Communicator {

	Channel getCommunicationChannel();

	void setCommunicationChannel(Channel new_channel);

}
