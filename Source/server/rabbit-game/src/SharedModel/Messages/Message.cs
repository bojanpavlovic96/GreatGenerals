
namespace RabbitGameServer.SharedModel.Messages
{
	public class Message
	{
		public MessageType type { get; set; }

		public string roomName { get; set; }
		public string username { get; set; }

		public Message(MessageType type, string player, string roomName)
		{
			this.type = type;

			this.username = player;
			this.roomName = roomName;
		}

	}
}