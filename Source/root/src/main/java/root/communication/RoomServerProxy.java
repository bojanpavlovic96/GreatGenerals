package root.communication;

public interface RoomServerProxy {

	void CreateRoom(String roomName, String password, String playerName,
			RoomServerResponseHandler handler);

	void JoinRoom(String roomName, String password, String playerName,
			RoomServerResponseHandler handler);

	void clearHandlers();

	boolean isReady();

	boolean alreadyInUse();

}
