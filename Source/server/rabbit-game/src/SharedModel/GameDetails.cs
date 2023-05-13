namespace RabbitGameServer.SharedModel
{

	public class GameDetails
	{
		public string gameId { get; set; }

		public string roomName { get; set; }
		public string master { get; set; }
		public string winner { get; set; }
		public double msDuration { get; set; }
		public DateTime startDate { get; set; }
		public int pointsGain { get; set; }

		public GameDetails(string gameId,
			string roomName,
			string master,
			string winner,
			double duration,
			DateTime startDate,
			int pointsGain)
		{
			this.gameId = gameId;
			this.roomName = roomName;
			this.master = master;
			this.winner = winner;
			this.msDuration = duration;
			this.startDate = startDate;
			this.pointsGain = pointsGain;
		}
	}
}