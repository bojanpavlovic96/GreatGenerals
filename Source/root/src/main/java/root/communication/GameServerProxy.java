package root.communication;

import root.command.CommandProducer;
import root.model.event.ModelEventArg;

public interface GameServerProxy extends CommandProducer {

	void sendIntention(ModelEventArg action);

}
