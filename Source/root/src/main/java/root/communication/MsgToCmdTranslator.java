package root.communication;

import root.command.Command;
import root.communication.messages.Message;
import root.model.event.ModelEventArg;

public interface MsgToCmdTranslator {

	Command ToCommand(Message message);

	Message ToMessage(ModelEventArg eventArg);

}
