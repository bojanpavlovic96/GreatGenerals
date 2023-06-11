
namespace RabbitGameServer.SharedModel.Messages
{
	public class LeaveGameMessage : Message
	{
		public LeaveGameMessage(DateTime timestamp, string username, string roomName)
			: base(MessageType.LeaveGame, timestamp, username, roomName)
		{
		}
	}
}