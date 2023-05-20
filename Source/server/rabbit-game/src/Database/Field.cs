
namespace RabbitGameServer.Database
{
	public class Field
	{
		public Point2D position;

		public bool isVisible;

		public string unitName;

		public Terrain terrain;

		public bool inBattle;

		public string owner;

		public Field(Point2D position, bool isVisible, string unitName, Terrain terrain, bool inBattle, string owner)
		{
			this.position = position;
			this.isVisible = isVisible;
			this.unitName = unitName;
			this.terrain = terrain;
			this.inBattle = inBattle;
			this.owner = owner;
		}
	}
}