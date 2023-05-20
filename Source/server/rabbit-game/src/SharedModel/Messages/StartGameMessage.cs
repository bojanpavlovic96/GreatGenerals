
namespace RabbitGameServer.SharedModel.Messages
{
	public class StartGameMessage : Message
	{
		public StartGameMessage(DateTime timestamp, string player, string roomName)
			: base(MessageType.StartGameRequest, timestamp, player, roomName)
		{
		}
	}
}