
namespace RabbitGameServer.SharedModel.Messages
{
	public class StartGameMessage : Message
	{
		public StartGameMessage(string player, string roomName)
			: base(MessageType.StartGameRequest, player, roomName)
		{
		}
	}
}