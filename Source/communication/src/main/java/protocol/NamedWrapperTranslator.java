package protocol;

import root.communication.ProtocolTranslator;
import root.communication.messages.Message;
import root.communication.parser.DataParser;

public class NamedWrapperTranslator implements ProtocolTranslator {

	private NameTypeResolver typeResolver;
	private DataParser parser;

	public NamedWrapperTranslator(
			DataParser parser,
			NameTypeResolver messageTypeResolver) {

		this.parser = parser;
		this.typeResolver = messageTypeResolver;
	}

	@Override
	public String toStrData(Message message) {
		var wrapper = new NamedWrapper(
				message.name,
				parser.ToString(message));

		return parser.ToString(wrapper);
	}

	@Override
	public byte[] toByteData(Message message) {
		return this.toStrData(message).getBytes();
	}

	@Override
	public Message toMessage(String strData) {
		var wrappedData = parser.FromString(strData, NamedWrapper.class);
		var messageType = typeResolver.resolve(wrappedData.name);
		if (messageType == null) {
			// TODO I guess this should be handled somehow
			// some exception maybe ...
			// debug
			System.out.println("Unknown message type received: " + wrappedData.name);
			return null;
		}

		return (Message) parser.FromString(wrappedData.payload, messageType);
	}

	@Override
	public Message toMessage(byte[] byteData) {
		return this.toMessage(new String(byteData));
	}

}
