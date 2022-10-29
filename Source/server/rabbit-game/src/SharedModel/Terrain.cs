namespace RabbitGameServer.SharedModel
{
	public enum TerrainType
	{
		mountains,
		watter
	}

	public class Terrain
	{
		public TerrainType type;
		public int intensity;

		public Terrain(TerrainType type, int intensity)
		{
			this.type = type;
			this.intensity = intensity;
		}
	}
}