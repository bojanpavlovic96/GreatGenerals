package server;

import root.model.event.ModelEventArg;

// also used for ServerProxy in GameController

public interface Server {

	// used from players to send their intentions
	// server than check are they valid from global perspective (based on the other
	// players positions)
	void sendIntention(ModelEventArg model_event);

}
