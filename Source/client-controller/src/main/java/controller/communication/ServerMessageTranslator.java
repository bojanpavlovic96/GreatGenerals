package controller.communication;

import root.command.Command;
import root.model.event.ModelEventArg;

public interface ServerMessageTranslator {

	public abstract Command translate(byte[] source);

	public abstract byte[] translate(ModelEventArg model_action);

}
