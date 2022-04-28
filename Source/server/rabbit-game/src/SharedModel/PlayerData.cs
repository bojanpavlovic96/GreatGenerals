
namespace RabbitGameServer.SharedModel
{
	public class PlayerData
	{
		public string username;
		public Color color;

		public PlayerData(string username, Color color)
		{
			this.username = username;
			this.color = color;
		}
	}
}