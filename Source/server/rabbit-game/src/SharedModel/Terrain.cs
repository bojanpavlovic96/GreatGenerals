namespace RabbitGameServer.SharedModel
{
	public class Terrain
	{
		public String name;
		public int intensity;

		public Terrain(string name, int intensity)
		{
			this.name = name;
			this.intensity = intensity;
		}
	}
}