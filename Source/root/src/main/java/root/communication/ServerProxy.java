package root.communication;

import root.command.CommandProducer;
import root.model.event.ModelEventArg;

public interface ServerProxy extends Communicator, CommandProducer {

	void sendIntention(ModelEventArg action);

}
