namespace RabbitGameServer.SharedModel
{
	public class Terrain
	{
		public TerrainType type;

		public float intensity;

		public Terrain(TerrainType type, float intensity)
		{
			this.type = type;
			this.intensity = intensity;
		}
	}
}