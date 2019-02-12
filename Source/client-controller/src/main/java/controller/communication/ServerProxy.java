package controller.communication;

import com.rabbitmq.client.Channel;

import controller.command.CtrlCommandQueue;
import server.Server;

public class ServerProxy implements Server, Communicator {

	private Channel channel;

	private ServerMessageTranslator message_translator;

	private CtrlCommandQueue ctrl_queue;

	// constructors

	public ServerProxy(Channel channel, ServerMessageTranslator translator) {

		this.channel = channel;
		this.message_translator = translator;

		this.ctrl_queue = new CtrlCommandQueue();

		this.initCommunicationChannel();

	}

	// methods

	private void initCommunicationChannel() {

	}

	// server methods

	// TODO add argument ... what to send
	public void sendIntention() {

	}

	// communicator methods

	public Channel getCommunicationChannel() {
		return this.channel;
	}

	public CtrlCommandQueue getCtrlQueue() {
		return this.ctrl_queue;
	}

}
