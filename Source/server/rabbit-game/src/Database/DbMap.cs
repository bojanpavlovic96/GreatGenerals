
namespace RabbitGameServer.Database
{
	public class DbMap
	{
		public string theme{get;set;}

		public int playersCount { get; set; }
		public List<Field> fields { get; set; }
		public List<List<Point2D>> playerPositions { get; set; }

	}
}