package proxy;

import com.rabbitmq.client.Channel;

public interface RabbitChannelProvider {

	public Channel getChannel();

	public boolean isConnected();

	public void subscribeForEvents(ConnectionEventHandler newSub);

	public void unsubscribeForEvents(ConnectionEventHandler newSub);

}
