package root.communication;

import root.communication.messages.RoomResponseMsg;

public interface RoomServerResponseHandler {
	void handle(RoomResponseMsg response);
}
