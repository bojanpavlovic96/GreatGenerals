namespace RabbitGameServer.SharedModel.Messages
{
	public enum RoomResponseType
	{
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
}