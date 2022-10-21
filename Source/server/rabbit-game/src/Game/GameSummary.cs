namespace RabbitGameServer.Game
{

	public class GameSummary
	{
		public int PlayersCount { get; set; }
		public List<string> Players { get; set; }
		public int EventsReceivedCount { get; set; }
		public int CommandsSentCount { get; set; }

		public GameSummary(int playersCount,
			List<string> players,
			int eventsReceivedCount,
			int commandsSentCount)
		{
			PlayersCount = playersCount;
			Players = players;
			EventsReceivedCount = eventsReceivedCount;
			CommandsSentCount = commandsSentCount;
		}
	}

}