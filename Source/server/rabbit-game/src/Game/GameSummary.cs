namespace RabbitGameServer.Game
{

	public class GameSummary
	{

		public string RoomName { get; set; }
		public string MasterPlayer { get; set; }

		public int PlayersCount { get; set; }
		public List<string> Players { get; set; }
		public int EventsReceivedCount { get; set; }
		public int CommandsSentCount { get; set; }

		public GameSummary(string roomName,
			string masterPlayer,
			int playersCount,
			List<string> players,
			int eventsReceivedCount,
			int commandsSentCount)
		{

			RoomName = roomName;
			MasterPlayer = masterPlayer;

			PlayersCount = playersCount;
			Players = players;
			EventsReceivedCount = eventsReceivedCount;
			CommandsSentCount = commandsSentCount;
		}
	}

}