namespace RabbitGameServer.SharedModel.Messages
{
	public class InitializeMessage : Message
	{

		public List<PlayerData> players;
		public List<Field> fields;

		public InitializeMessage(string roomName,
			List<PlayerData> players,
			List<Field> fields)
			: base(MessageType.InitializeMessage, "", roomName)
		{

			this.players = players;
			this.fields = fields;
		}
	}
}