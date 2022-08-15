
namespace RabbitGameServer.Game
{
	public class PoolSummary
	{

		public int id { get; set; }
		public string startedDate { get; set; }
		public int roomsCount { get; set; }
		public int playersCount { get; set; }

		public PoolSummary(int id, string startedDate, int roomsCount, int playersCount)
		{
			this.id = id;
			this.startedDate = startedDate;
			this.roomsCount = roomsCount;
			this.playersCount = playersCount;
		}
	}
}