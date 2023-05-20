namespace RabbitGameServer.SharedModel.Messages
{
	public class InitializeMessage : Message
	{

		public List<PlayerData> players { get; set; }
		public List<Move> moves { get; set; }
		public List<Unit> units { get; set; }
		public List<Attack> attacks { get; set; }
		public List<Field> fields { get; set; }

		public InitializeMessage(DateTime timestamp, 
			string roomName,
			string username,
			List<PlayerData> players,
			List<Move> moves,
			List<Unit> units,
			List<Attack> attacks,
			List<Field> fields)
			: base(MessageType.InitializeMessage,timestamp, username, roomName)
		{

			this.players = players;

			this.moves = moves;
			this.units = units;
			this.attacks = attacks;

			this.fields = fields;
		}


	}
}