namespace RabbitGameServer.Database
{
	public class Command
	{
		public string name { get; set; }

		public string playerName { get; set; }

		public Point2D fromField { get; set; }
		public Point2D toField { get; set; }

	}
}