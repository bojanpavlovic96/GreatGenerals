
namespace RabbitGameServer.SharedModel.Messages
{
	public class Message
	{
		public MessageType type { get; set; }

		public string roomName { get; set; }
		public string player { get; set; }

		public Message(MessageType type, string player, string roomName)
		{
			this.type = type;

			this.player = player;
			this.roomName = roomName;
		}

	}
}