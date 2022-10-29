namespace RabbitGameServer.SharedModel.Messages
{
	public class InitializeMessage : Message
	{

		public List<PlayerData> players;
		public List<Field> fields;

		public InitializeMessage(string roomName,
			string username,
			List<PlayerData> players,
			List<Field> fields)
			: base(MessageType.InitializeMessage, username, roomName)
		{

			this.players = players;
			this.fields = fields;
		}
	}
}