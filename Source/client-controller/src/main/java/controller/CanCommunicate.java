package controller;

import communication.Communicator;

public interface CanCommunicate {

	Communicator getCommunicator();

	void setCommunicator(Communicator communicator);

}
