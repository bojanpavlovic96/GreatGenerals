
namespace RabbitGameServer.SharedModel.Messages
{
	public class Message
	{
		public string name { get; set; }

		public string roomName { get; set; }
		public string player { get; set; }

		public Message(string name, string player, string roomName)
		{
			this.name = name;

			this.player = player;
			this.roomName = roomName;
		}

	}
}