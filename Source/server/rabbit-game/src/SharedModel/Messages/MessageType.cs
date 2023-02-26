namespace RabbitGameServer.SharedModel.Messages
{
	public enum MessageType
	{
		ReadyForInitMsg,
		InitializeMessage,
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
		ServerErrorMessage
	}
}