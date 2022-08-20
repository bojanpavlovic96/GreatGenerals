namespace RabbitGameServer.SharedModel.Messages
{
	public enum MessageType
	{
		InitializeMessage,
		MoveCommand,
		CreateRoomRequest,
		JoinRoomRequest,
		LeaveRoomRequest,
		JoinResponse
	}
}