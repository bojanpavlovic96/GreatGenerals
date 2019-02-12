package controller.communication;

import com.rabbitmq.client.Channel;

public interface Communicator {

	Channel getCommunicationChannel();

	// TODO maybe also setCommunicatoinChannel
	// will support some kind of distributed system ... or not
	// changing communicationChannel from the run-time

}
