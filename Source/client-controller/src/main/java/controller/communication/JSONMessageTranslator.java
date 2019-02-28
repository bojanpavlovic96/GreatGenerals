package controller.communication;

import controller.communication.ServerMessage;
import controller.communication.ServerMessageTranslator;

public class JSONMessageTranslator extends ServerMessageTranslator {

	public JSONMessageTranslator() {
		super();

	}

	// attention this method should return ctrlCommand
	@Override
	public ServerMessage translate(byte[] source) {

		return null;
	}

}
