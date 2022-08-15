namespace RabbitGameServer.Game
{

	public class GameSummary
	{
		public int PlayersCount { get; set; }
		public int EventsReceivedCount { get; set; }
		public int CommandsSentCount { get; set; }

		public GameSummary(int playersCount, int eventsReceivedCount, int commandsSentCount)
		{
			PlayersCount = playersCount;
			EventsReceivedCount = eventsReceivedCount;
			CommandsSentCount = commandsSentCount;
		}
	}

}