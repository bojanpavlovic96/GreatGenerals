package root.communication.messages;

public enum MessageType {
	CreateRoomRequest,
	JoinRoomRequest,
	LeaveRoomRequest,
	RoomResponse,
	StartGameRequest,
	InitializeMessage,
	ReadyForInitMsg,
	ReadyForReplayMsg,
	ReplayMessage,
	MoveMessage,
	AttackMessage,
	DefendMessage,
	RecalculatePathMessage,
	AbortMoveMessage,
	AbortAttackMessage,
	AbortDefenseMessage,
	ServerErrorMessage,
	IncomeTick,
	PointsUpdate,
	BuildUnit,
	GameDone,
	LeaveGame,
	RemovePlayer
}
