namespace RabbitGameServer.SharedModel.Messages
{
	public class InitializeMessage : Message
	{

		public List<PlayerData> players;
		public List<Move> moves;
		public List<Unit> units;
		public List<Attack> attacks;
		public List<Field> fields;

		public InitializeMessage(string roomName,
			string username,
			List<PlayerData> players,
			List<Move> moves,
			List<Unit> units,
			List<Attack> attacks,
			List<Field> fields)
			: base(MessageType.InitializeMessage, username, roomName)
		{

			this.players = players;

			this.moves = moves;
			this.units = units;
			this.attacks = attacks;

			this.fields = fields;
		}


	}
}