package controller.communication;

import model.event.ModelEvent;

public abstract class ServerMessageTranslator {

	public ServerMessageTranslator() {
		
	}

	public abstract ServerMessage translate(byte[] source);

	public abstract byte[] translate(ModelEvent model_action);

}
