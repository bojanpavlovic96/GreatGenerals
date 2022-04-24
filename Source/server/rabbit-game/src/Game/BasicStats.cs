namespace RabbitGameServer.Game
{

	public class BasicStats
	{
		public int PlayersCount { get; set; }
		public int EventsReceivedCount { get; set; }
		public int CommandsSentCount { get; set; }


		public BasicStats()
		{
			PlayersCount = 0;
			EventsReceivedCount = 0;
			CommandsSentCount = 0;
		}

		public void Add(BasicStats stats)
		{
			this.PlayersCount += stats.PlayersCount;
			this.EventsReceivedCount += stats.EventsReceivedCount;
			this.CommandsSentCount += stats.CommandsSentCount;
		}

	}

}