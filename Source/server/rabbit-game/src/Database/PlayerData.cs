
using RabbitGameServer.SharedModel;

namespace RabbitGameServer.Database
{
	public class PlayerData
	{
		public string username { get; set; }
		public Color color { get; set; }

		public int level { get; set; }
		public int points { get; set; }

		public PlayerData(string username, Color color, int level, int points)
		{
			this.username = username;
			this.color = color;
			this.level = level;
			this.points = points;
		}
	}
}