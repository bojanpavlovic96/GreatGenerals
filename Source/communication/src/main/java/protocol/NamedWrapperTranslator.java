package protocol;

import root.communication.ProtocolTranslator;
import root.communication.messages.Message;
import root.communication.messages.MessageType;
import root.communication.parser.DataParser;

public class NamedWrapperTranslator implements ProtocolTranslator {

	private NameTypeResolver typeResolver;
	private DataParser parser;

	public NamedWrapperTranslator(DataParser parser, NameTypeResolver messageTypeResolver) {

		this.parser = parser;
		this.typeResolver = messageTypeResolver;
	}

	@Override
	public String toStrData(Message message) {
		var wrapper = new NamedWrapper(
				message.type.toString(),
				parser.ToString(message));

		return parser.ToString(wrapper);
	}

	@Override
	public byte[] toByteData(Message message) {
		return this.toStrData(message).getBytes();
	}

	@Override
	public Message toMessage(String strData) {

		Message message = null;

		try {
			var wrappedData = parser.FromString(strData, NamedWrapper.class);
			var messageType = typeResolver.resolve(MessageType.valueOf(wrappedData.name));

			if (messageType != null) {
				System.out.println("Resolved message class: " + messageType.toString());
				message = (Message) parser.FromString(wrappedData.payload, messageType);
			} else {
				System.out.println("Unknown message type received: " + wrappedData.name);
			}

		} catch (Exception e) {
			System.out.println("exception while unwrapping/translating/casting received message ... ");
			System.out.println(e.getMessage());
		}

		return message;
	}

	@Override
	public Message toMessage(byte[] byteData) {
		return this.toMessage(new String(byteData));
	}

}
