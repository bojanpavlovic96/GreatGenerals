namespace RabbitGameServer.Database
{
	public class Move
	{
		public string type { get; set; }

		public int range { get; set; }
		public long speed { get; set; }
		public float terrainMultiplier { get; set; }

		public Move(string type, int range, long speed, float terrainMultiplier)
		{
			this.type = type;
			this.range = range;
			this.speed = speed;
			this.terrainMultiplier = terrainMultiplier;
		}
	}
}