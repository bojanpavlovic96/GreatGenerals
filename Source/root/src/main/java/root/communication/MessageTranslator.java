package root.communication;

import root.command.Command;
import root.model.event.ModelEventArg;

public interface MessageTranslator {

	Command toCommand(byte[] source);

	byte[] toByte(ModelEventArg model_action);

}
