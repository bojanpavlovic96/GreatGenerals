package root.communication.messages;

// used for single action responses and room updates
public enum RoomResponseType {
	Success,
	InvalidRoom,
	InvalidPlayer,
	WrongPassword,
	AlreadyIn,
	UnknownFail,
	PlayerJoined,
	PlayerLeft,
	RoomDestroyed,
	GameNotReady,
	GameStarted
}
