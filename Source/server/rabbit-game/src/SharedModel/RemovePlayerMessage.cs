
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.SharedModel
{
	public class RemovePlayerMessage : Message
	{

		public string whoLeft { get; set; }

		public RemovePlayerMessage(DateTime timestamp, string username, string roomName, string whoLeft)
			: base(MessageType.RemovePlayer, timestamp, username, roomName)
		{
			this.whoLeft = whoLeft;
		}
	}
}