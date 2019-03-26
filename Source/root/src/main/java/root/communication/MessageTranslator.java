package root.communication;

import root.command.Command;
import root.model.event.ModelEventArg;

public interface MessageTranslator {

	Command translate(byte[] source);

	byte[] translate(ModelEventArg model_action);

}
