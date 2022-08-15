package proxy;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;

import root.communication.ProtocolTranslator;
import root.communication.RoomServerResponseHandler;
import root.communication.messages.RoomResponseMsg;

import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class RoomResponseConsumer implements Consumer {

	private RabbitRoomServerProxy proxy;

	private RoomServerResponseHandler handler;
	private ProtocolTranslator translator;

	public RoomResponseConsumer(RabbitRoomServerProxy proxy,
			RoomServerResponseHandler handler,
			ProtocolTranslator translator) {

		this.proxy = proxy;
		this.handler = handler;

		this.translator = translator;
	}

	// region unused

	@Override
	public void handleConsumeOk(String consumerTag) {
	}

	@Override
	public void handleCancelOk(String consumerTag) {
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

	}

	@Override
	public void handleRecoverOk(String consumerTag) {

	}

	// endregion

	@Override
	public void handleDelivery(String consumerTag,
			Envelope envelope,
			BasicProperties properties,
			byte[] body) throws IOException {

		System.out.println("Received room response message ... ");
		System.out.println(new String(body));

		var message = translator.toMessage(body);
		handler.handle((RoomResponseMsg) message);

		proxy.clearResponseAwaiter();
		return;
	}

}
