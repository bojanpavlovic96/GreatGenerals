namespace RabbitGameServer.SharedModel.Messages
{
	public enum RoomResponseType
	{
		Success,
		// ServerBusy,
		InvalidRoom,
		InvalidPlayer,
		WrongPassword,
		AlreadyIn,
		UnknownFail,
		PlayerJoined,
		PlayerLeft,
		RoomDestroyed,
		GameNotReady,
		GameStarted
	}
}