package proxy;

public interface ConnectionEventHandler {

	void handleConnectionEvent(RabbitChannelProvider channelProvider,
			RabbitConnectionEventType eventType);

}
