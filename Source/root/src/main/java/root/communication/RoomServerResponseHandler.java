package root.communication;

import root.communication.messages.JoinResponseMsg;

public interface RoomServerResponseHandler {
	void handle(JoinResponseMsg response);
}
