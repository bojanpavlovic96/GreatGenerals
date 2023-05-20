
namespace RabbitGameServer.Database
{
	public class Terrain
	{
		public string type{get;set;}
		public float intensity{get;set;}

		public Terrain(string type, float intensity)
		{
			this.type = type;
			this.intensity = intensity;
		}
	}
}