namespace RabbitGameServer.SharedModel.Messages
{
	public enum MessageType
	{
		ReadyForInitMsg,
		ReadyForReplayMsg,
		InitializeMessage,
		ReplayMessage,
		MoveMessage,
		CreateRoomRequest,
		JoinRoomRequest,
		RoomResponse,
		LeaveRoomRequest,
		StartGameRequest,
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
		GameDone
	}
}