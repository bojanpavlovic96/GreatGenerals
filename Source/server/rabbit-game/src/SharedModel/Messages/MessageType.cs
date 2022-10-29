namespace RabbitGameServer.SharedModel.Messages
{
	public enum MessageType
	{
		CreateRoomRequest,
		JoinRoomRequest,
		LeaveRoomRequest,
		JoinResponse, // this should be renamed to roomResponse
		StartGameRequest,
		InitializeMessage,
		ReadyForInitMsg,
		MoveMessage,
		AttackMessage,
		RecalculatePathMessage,
		AbortMoveMessage,
		ServerErrorMessage
	}
}