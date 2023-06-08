package root.communication.messages;

// used for single action responses and room updates
public enum RoomResponseType {
	Success,
	// ServerBusy,
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
