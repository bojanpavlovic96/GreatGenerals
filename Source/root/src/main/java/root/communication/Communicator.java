package root.communication;

import com.rabbitmq.client.Channel;

public interface Communicator {

	Channel getCommunicationChannel();

	void setCommunicationChannel(Channel new_channel);

	MessageTranslator getMessageTranslator();

	void setMessageTranslator(MessageTranslator new_translator);

}
