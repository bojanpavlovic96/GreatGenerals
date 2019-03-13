package server;

import model.event.ModelEvent;

// also used for ServerProxy in GameController

public interface Server {

	// used from players to send their intentions
	// server than check are they valid from global perspective (based on the other
	// players positions)
	void sendIntention(ModelEvent model_event);

}
