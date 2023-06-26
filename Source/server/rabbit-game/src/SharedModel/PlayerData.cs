
namespace RabbitGameServer.SharedModel
{
	public class PlayerData
	{
		public string username { get; set; }
		public Color color { get; set; }

		public int level { get; set; }
		public int points { get; set; }
		public int coins{get;set;}

		// required for serialization 
		public PlayerData()
		{

		}

		public PlayerData(string username, Color color)
		{
			this.username = username;
			this.color = color;
		}

		public PlayerData(string username, Color color, int level, int points)
			: this(username, color)
		{
			this.level = level;
			this.points = points;
		}

	}
}