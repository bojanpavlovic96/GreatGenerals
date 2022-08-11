package root.communication;

public interface RoomServerProxy {

	void CreateRoom(String roomName, String password, String playerName,
			RoomServerResponseHandler responseHandler,
			RoomServerResponseHandler newJoinHandler);

	void JoinRoom(String roomName, String password, String playerName,
			RoomServerResponseHandler handler);

	void LeaveRoom(String roomName, String username, RoomServerResponseHandler handler);

	void clearHandlers();

	boolean isReady();

	boolean alreadyInUse();

}
