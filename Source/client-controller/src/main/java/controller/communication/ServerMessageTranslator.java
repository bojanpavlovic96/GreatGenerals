package controller.communication;

import model.event.ModelEventArg;

public abstract class ServerMessageTranslator {

	public ServerMessageTranslator() {
		
	}

	public abstract ServerMessage translate(byte[] source);

	public abstract byte[] translate(ModelEventArg model_action);

}
