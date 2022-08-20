package root.communication;

public interface RoomServerProxy {

	void CreateRoom(String roomName, String password, String playerName,
			RoomServerResponseHandler responseHandler);

	void JoinRoom(String roomName, String password, String playerName,
			RoomServerResponseHandler handler);

	void LeaveRoom(String roomName, String username, RoomServerResponseHandler handler);

	void SubscribeForRoomUpdates(String roomName, String username,
			RoomServerResponseHandler handler);

	void UnsubFromRoomUpdates();

	boolean isReady();

	void clearResponseAwaiter();

	boolean isWaitingForResponse();

	boolean isReceivingUpdates();

}
