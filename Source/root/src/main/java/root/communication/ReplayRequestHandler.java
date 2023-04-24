package root.communication;

import root.communication.messages.ReplayServerResponse;

public interface ReplayRequestHandler {
	void handle(ReplayServerResponse response);
}
