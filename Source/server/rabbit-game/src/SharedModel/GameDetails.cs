
namespace RabbitGameServer.SharedModel
{

	public enum GameStatus
	{
		Won,
		Lost,
		Unknown
	}

	public class GameDetails
	{
		public string gameId { get; set; }

		public string roomName { get; set; }
		public string master { get; set; }
		public string winner { get; set; }
		public double msDuration { get; set; }
		public GameStatus status { get; set; }
		public int pointsGain { get; set; }

		public GameDetails(string gameId, string roomName, string master, int duration, GameStatus status, int pointsGain)
		{
			this.gameId = gameId;
			this.roomName = roomName;
			this.master = master;
			this.msDuration = duration;
			this.status = status;
			this.pointsGain = pointsGain;
		}
	}
}