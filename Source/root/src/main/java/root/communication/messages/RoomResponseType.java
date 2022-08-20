package root.communication.messages;

// use form single action responses and room updates
public enum RoomResponseType {
	Success,
	InvalidRoom,
	InvalidPlayer,
	WrongPassword,
	AlreadyIn,
	UnknownFail,
	PlayerJoined,
	PlayerLeft,
	RoomDestroyed
}
