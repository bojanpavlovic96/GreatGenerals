package rabbit;

import com.rabbitmq.client.Channel;

import communication.Communicator;

public class Messenger implements Communicator {

	private Channel channel;

	public Messenger(Channel channel) {
		super();

		this.channel = channel;

	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
