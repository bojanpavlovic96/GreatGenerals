package root.communication;

import root.communication.messages.Message;

public interface ProtocolTranslator {

	String toStrData(Message message);

	byte[] toByteData(Message message);

	Message toMessage(String strData);

	Message toMessage(byte[] byteData);

}
