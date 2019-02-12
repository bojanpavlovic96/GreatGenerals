package controller.communication;

import controller.communication.ServerMessage;

public abstract class ServerMessageTranslator {

	public ServerMessageTranslator() {

	}

	public abstract ServerMessage translate(byte[] source);

}
