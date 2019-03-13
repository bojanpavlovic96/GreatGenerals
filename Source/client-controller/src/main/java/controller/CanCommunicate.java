package controller;

import controller.communication.Communicator;
import server.Server;

public interface CanCommunicate {

	Communicator getCommunicator();

	// attention maybe this is not the best approach

	Server getServer();

}
