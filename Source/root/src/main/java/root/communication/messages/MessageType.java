package root.communication.messages;

public enum MessageType {
	CreateRoomRequest,
	JoinRoomRequest,
	LeaveRoomRequest,
	JoinResponse,
	StartGameRequest,
	InitializeMsg,
	ReadyForInitMsg,
	MoveMessage,
	AttackMessage,
	RecalculatePathMessage,
	AbortMoveMessage,
	ServerErrorMessage
}
