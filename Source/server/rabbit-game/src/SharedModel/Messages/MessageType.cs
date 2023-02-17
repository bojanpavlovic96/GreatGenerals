namespace RabbitGameServer.SharedModel.Messages
{
	public enum MessageType
	{
		ReadyForInitMsg,
		InitializeMessage,
		MoveMessage,
		CreateRoomRequest,
		JoinRoomRequest,
		RoomResponse, // this should be renamed to roomResponse
		LeaveRoomRequest,
		StartGameRequest,
		AttackMessage,
		RecalculatePathMessage,
		AbortMoveMessage,
		ServerErrorMessage
	}
}