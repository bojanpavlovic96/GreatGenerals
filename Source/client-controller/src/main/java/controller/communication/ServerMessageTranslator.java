package controller.communication;

import controller.command.CtrlCommand;
import model.event.ModelEventArg;

public interface ServerMessageTranslator {

	public abstract CtrlCommand translate(byte[] source);

	public abstract byte[] translate(ModelEventArg model_action);

}
