package root.communication.messages;

public enum MessageType {
	CreateRoomRequest,
	JoinRoomRequest,
	LeaveRoomRequest,
	RoomResponse,
	StartGameRequest,
	InitializeMessage,
	ReadyForInitMsg,
	MoveMessage,
	AttackMessage,
	DefendMessage,
	RecalculatePathMessage,
	AbortMoveMessage,
	AbortAttackMessage,
	AbortDefenseMessage,
	ServerErrorMessage
}
