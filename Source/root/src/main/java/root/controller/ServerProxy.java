package root.controller;

import root.Communicator;
import root.model.event.ModelEventArg;

public interface ServerProxy extends Communicator {

	void sendIntention(ModelEventArg action);

}
